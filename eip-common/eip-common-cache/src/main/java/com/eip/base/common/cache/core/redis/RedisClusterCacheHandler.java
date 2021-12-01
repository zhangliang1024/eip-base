package com.eip.base.common.cache.core.redis;

import com.eip.base.common.cache.config.CacheConstact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 17:36
 * @Version: V1.0.0
 * @Description: 基于Redis实现分布式缓存
 */
public class RedisClusterCacheHandler extends ClusterCacheHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public <T extends Object> boolean set(String key, T value) {
        //设置缓存不设置过期时间
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    @Override
    public <T extends Object> boolean set(String key, T value, Long timeOut, TimeUnit unit) {
        timeOut = expireTime(timeOut);
        if (CacheConstact.DEFAULT_FOREVER_CACHE == timeOut) {
            set(key, value);
        } else {
            //设置缓存且设置过期时间
            redisTemplate.opsForValue().set(key, value, timeOut, unit);
        }
        return true;
    }

    @Override
    public <V extends Object> V get(String key) {
        return (V) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <V extends Object> V get(String key, Long expireTime) {
        return get(key);
    }

    @Override
    public boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public boolean remove(String key, Long timeOut) {
        return remove(key);
    }

}
