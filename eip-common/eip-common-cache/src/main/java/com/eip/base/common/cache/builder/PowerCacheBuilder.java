package com.eip.base.common.cache.builder;

import com.eip.base.common.cache.config.CacheConstact;
import com.eip.base.common.cache.config.CacheProperties;
import com.eip.base.common.cache.core.BaseCacheHandler;
import com.eip.base.common.cache.core.guava.MemoryCacheHandler;
import com.eip.base.common.cache.core.redis.ClusterCacheHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Barnett
 * @Date: 2021/11/30 16:07
 * @Version: V1.0.0
 * @Description:
 */
@Slf4j
public class PowerCacheBuilder {

    @Autowired
    private CacheProperties cacheProperties;
    @Autowired
    private ClusterCacheHandler clusterCacheHandler;
    @Autowired
    private MemoryCacheHandler memoryCacheHandler;

    private static List<BaseCacheHandler> _listCacheProvider = Lists.newArrayList();

    private static final Lock providerLock = new ReentrantLock();


    /**
     * 初始化缓存实现，优先级：先本地后分布式
     *
     * @return
     */
    private List<BaseCacheHandler> getCacheProviders() {
        if (!CollectionUtils.isEmpty(_listCacheProvider)) {
            return _listCacheProvider;
        }
        try {
            providerLock.tryLock(1000, TimeUnit.MILLISECONDS);
            if (!CollectionUtils.isEmpty(_listCacheProvider)) {
                return _listCacheProvider;
            }
            //启用本地缓存
            if (checkUserMemoryCache()) {
                _listCacheProvider.add(memoryCacheHandler);
            }
            //启用分布式缓存
            if (checkUserClusterCache()) {
                _listCacheProvider.add(clusterCacheHandler);
                //设置分布式缓存版本号
                resetCacheVersion();
            }
        } catch (Exception e) {

        } finally {
            providerLock.unlock();
        }
        return _listCacheProvider;
    }


    /**
     * Description: 查询缓存
     * 默认永久缓存
     *
     * @auther: Barnett
     * @date: 2021/11/30 17:39
     * @param:
     * @return:
     */
    public <T extends Object> T get(String key) {
        return get(key, CacheConstact.DEFAULT_FOREVER_CACHE);
    }

    public <T extends Object> T get(String key, Long expireTime) {
        T value = null;
        //带缓存版本的key
        key = generateVerKey(key);
        for (BaseCacheHandler provider : getCacheProviders()) {
            value = provider.get(key, expireTime);
            if (value != null) {
                return value;
            }
        }
        return value;
    }


    /**
     * Description: 保存到缓存
     * 不指定缓存时间，默认永久缓存
     * @auther: Barnett
     * @date: 2021/11/30 17:27
     * @param:
     * @return:
     */
    public <T extends Object> void set(String key, T value) {
        //带缓存版本的key
        key = generateVerKey(key);
        for (BaseCacheHandler provider : getCacheProviders()) {
            provider.set(key, value, CacheConstact.DEFAULT_FOREVER_CACHE, TimeUnit.SECONDS);
        }
    }


    /**
     * Description: 保存到缓存
     *
     * @auther: Barnett
     * @date: 2021/11/30 17:27
     * @param:
     * @return:
     */
    public <T extends Object> void set(String key, T value, Long expireTime) {
        //带缓存版本的key
        key = generateVerKey(key);
        for (BaseCacheHandler provider : getCacheProviders()) {
            provider.set(key, value, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * Description: 保存到缓存
     *
     * @auther: Barnett
     * @date: 2021/11/30 17:27
     * @param:
     * @return:
     */
    public <T extends Object> void set(String key, T value, Long expireTime, TimeUnit unit) {
        //带缓存版本的key
        key = generateVerKey(key);
        for (BaseCacheHandler provider : getCacheProviders()) {
            provider.set(key, value, expireTime, unit);
        }
    }


    /**
     * Description: 移除缓存
     *
     * @auther: Barnett
     * @date: 2021/11/30 17:25
     * @param:
     * @return:
     */
    public void remove(String key) {
        //带缓存版本的key
        key = generateVerKey(key);
        if (StringUtils.isEmpty(key)) {
            return;
        }
        for (BaseCacheHandler provider : getCacheProviders()) {
            provider.remove(key);
        }
    }


    /**
     * Description: 是否已缓存
     *
     * @auther: Barnett
     * @date: 2021/11/30 17:23
     * @param:
     * @return:
     */
    public boolean contains(String key) {
        //带缓存版本的key
        key = generateVerKey(key);
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        Object value = get(key);
        return Optional.ofNullable(value).isPresent();
    }


    /**
     * Description: 获取分布式缓存版本号
     *
     * @auther: Barnett
     * @date: 2021/11/30 17:08
     * @param:
     * @return:
     */
    public String getCacheVersion() {
        if (checkUserClusterCache()) {
            return (String) clusterCacheHandler.get(cacheProperties.getCacheVersionKey());
        }
        return Strings.EMPTY;
    }

    /**
     * Description: 重置缓存版本号
     * 启动分布式缓存，需要设置缓存版本号
     *
     * @auther: Barnett
     * @date: 2021/11/30 16:29
     * @param:
     * @return:
     */
    public String resetCacheVersion() {
        //未启用分布式缓存
        if (!checkUserClusterCache()) {
            return Strings.EMPTY;
        }
        //设置缓存版本
        String version = String.valueOf(Math.abs(UUID.randomUUID().hashCode()));
        clusterCacheHandler.set(cacheProperties.getCacheVersionKey(), version);
        return version;
    }


    /**
     * Description: 获取带缓存版本的key
     * 通过重置缓存版本，可以实现相对实时的缓存过期控制
     *
     * @auther: Barnett
     * @date: 2021/11/30 16:49
     * @param: 缓存key
     * @return:
     */
    public String generateVerKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return key;
        }
        //未启用分布式缓存，则直接返回
        if (!checkUserClusterCache()) {
            return key;
        }
        String version = (String) clusterCacheHandler.get(cacheProperties.getCacheVersionKey());
        if (StringUtils.isEmpty(version)) {
            return key;
        }
        return String.format("%s_%s", key, version);
    }


    /**
     * Description: 是否启用分布式缓存
     *
     * @auther: Barnett
     * @date: 2021/11/30 16:41
     * @param:
     * @return:
     */
    private boolean checkUserClusterCache() {
        if (cacheProperties.getClusterType() != null && CacheConstact.Cluster.REDIS.name().equals(cacheProperties.getClusterType().name())) {
            return true;
        }
        return false;
    }

    /**
     * Description: 是否启用分布式缓存
     *
     * @auther: Barnett
     * @date: 2021/11/30 16:41
     * @param:
     * @return:
     */
    private boolean checkUserMemoryCache() {
        if (cacheProperties.getMemoryType() != null && CacheConstact.Memory.GUAVA.name().equals(cacheProperties.getMemoryType().name())) {
            return true;
        }
        return false;
    }
}
