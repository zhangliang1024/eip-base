package com.eip.base.common.cache.core.guava;

import com.eip.base.common.cache.config.CacheConstact;
import com.google.common.cache.Cache;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 17:41
 * @Version: V1.0.0
 * @Description: 基于JVM内存的缓存策略
 */
public class GuavaMemoryCacheHandler extends MemoryCacheHandler {

    @Override
    public <T extends Object> boolean set(String key, T value) {
        set(key, value, CacheConstact.DEFAULT_CACHE_SEONDS, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public <T extends Object> boolean set(String key, T value, Long timeOut, TimeUnit unit) {
        Cache<String, Object> cache = currentCacheInstance(timeOut);
        //构建缓存对象
        CacheValue cacheValue = new CacheValue()
                .setValue(value)
                .setCreateTime(System.currentTimeMillis())
                .setTimeout(timeOut)
                .setUnit(unit);
        cache.put(String.valueOf(key), cacheValue);
        return true;
    }

    @Override
    public <T extends Object> T get(String key) {
        return get(key, CacheConstact.DEFAULT_CACHE_SEONDS);
    }

    @Override
    public <T extends Object> T get(String key, Long expireTime) {
        Cache<String, Object> cache = currentCacheInstance(expireTime);
        //获取value值
        CacheValue cacheValue = (CacheValue) cache.getIfPresent(key);
        if (cacheValue != null) {
            Long timeout = cacheValue.getTimeout();
            //-1表示永生
            if (timeout == CacheConstact.DEFAULT_FOREVER_CACHE) {
                return (T) cacheValue.getValue();
            }
            //判断当前是否已超时
            if (cacheValue.getCreateTime() + cacheValue.getUnit().toMillis(timeout) < System.currentTimeMillis()) {
                //已超时
                remove(key);
            } else {
                return (T) cacheValue.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean remove(String key) {
        remove(key, CacheConstact.DEFAULT_CACHE_SEONDS);
        return true;
    }

    @Override
    public boolean remove(String key, Long expireTime) {
        Cache<String, Object> cache = currentCacheInstance(expireTime);
        cache.invalidate(key);
        return true;
    }

    private Cache<String, Object> currentCacheInstance(Long expireTime) {
        //获取过期时间
        expireTime = expireTime(expireTime);
        //获取当前时间片的缓存实例
        return cacheContainer(expireTime);
    }
}
