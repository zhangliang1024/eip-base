package com.eip.base.common.cache.core.guava;

import com.eip.base.common.cache.config.CacheConstact;
import com.eip.base.common.cache.config.CacheProperties;
import com.eip.base.common.cache.core.BaseCacheHandler;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 17:40
 * @Version: V1.0.0
 * @Description: 基于内存规范 一级缓存
 */
public abstract class MemoryCacheHandler implements BaseCacheHandler {


    public static Map<String, Cache<String, Object>> _cacheMap = Maps.newConcurrentMap();

    @Autowired
    private CacheProperties cacheProperties;

    static {
        Cache<String, Object> cacheContainer = CacheBuilder.newBuilder()
                //并发级别，也就是可以同时操作的线程数
                .concurrencyLevel(CacheConstact.CACHE_CONCURRENCY_LEVEL)
                //最大容量
                .maximumSize(CacheConstact.CACHE_MAX_SIZE)
                //最后一次写入后的一段时间移出
                .expireAfterWrite(CacheConstact.DEFAULT_CACHE_SEONDS, TimeUnit.MILLISECONDS)
                //开启统计功能
                .recordStats()
                .build();
        _cacheMap.put(String.valueOf(CacheConstact.DEFAULT_CACHE_SEONDS), cacheContainer);
    }


    private static Lock lock = new ReentrantLock();

    public Cache<String, Object> cacheContainer(Long expireTime) {
        Cache<String, Object> cacheContainer = null;
        if (expireTime == null) {
            return cacheContainer;
        }

        String mapKey = String.valueOf(expireTime);
        if (_cacheMap.containsKey(mapKey) == true) {
            cacheContainer = _cacheMap.get(mapKey);
            return cacheContainer;
        }

        lock.lock();
        try {
            if (CacheConstact.DEFAULT_FOREVER_CACHE == expireTime.longValue()) {
                cacheContainer = CacheBuilder.newBuilder()
                        .concurrencyLevel(CacheConstact.CACHE_CONCURRENCY_LEVEL)
                        .maximumSize(cacheProperties.getCacheMaxSize())
                        .recordStats()
                        .build();
            } else {
                cacheContainer = CacheBuilder.newBuilder()
                        .concurrencyLevel(CacheConstact.CACHE_CONCURRENCY_LEVEL)
                        .maximumSize(cacheProperties.getCacheMaxSize())
                        .expireAfterWrite(expireTime, TimeUnit.SECONDS)
                        .recordStats()
                        .build();

            }
            _cacheMap.put(mapKey, cacheContainer);
        } finally {
            lock.unlock();
        }
        return cacheContainer;
    }


    /**
     * Description: 缓存过期时间
     * 单位：秒
     * 若缓存时间小于30秒，则最低缓存30s
     *
     * @auther: Barnett
     * @date: 2021/11/25 18:21
     */
    public Long expireTime(Long expireTime) {
        if (expireTime != null && expireTime.longValue() == CacheConstact.DEFAULT_FOREVER_CACHE) {
            return expireTime;
        } else if (expireTime == null || expireTime.longValue() < CacheConstact.DEFAULT_CACHE_MIN_SEONDS) {
            expireTime = CacheConstact.DEFAULT_CACHE_MIN_SEONDS;
        }
        return expireTime;
    }


    /**
     * 缓存对象
     *
     * @param <T>
     */
    @Data
    @Accessors(chain = true)
    public static class CacheValue<T> {
        //缓存的值
        T value;
        //超时时间
        Long timeout;
        //单位
        TimeUnit unit;
        //创建时间
        Long createTime;
    }


    @Override
    public boolean contains(String key) {
        if (Optional.ofNullable(key).isPresent()) {
            return false;
        }
        Object value = get(key);
        return Optional.ofNullable(value).isPresent();
    }
}
