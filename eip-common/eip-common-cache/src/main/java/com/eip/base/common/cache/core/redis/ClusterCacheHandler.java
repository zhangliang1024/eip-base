package com.eip.base.common.cache.core.redis;

import com.eip.base.common.cache.config.CacheConstact;
import com.eip.base.common.cache.core.BaseCacheHandler;

import java.util.Optional;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 17:34
 * @Version: V1.0.0
 * @Description: 分布式缓存接口，二级缓存
 */
public abstract class ClusterCacheHandler implements BaseCacheHandler {

    /**
     * Description: 缓存过期时间
     * 单位：秒
     * 若缓存时间小于30秒，则最低缓存30s
     * -1 : 表示永不过期
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

    @Override
    public boolean contains(String key) {
        if (Optional.ofNullable(key).isPresent()) {
            return false;
        }
        Object value = get(key);
        return Optional.ofNullable(value).isPresent();
    }
}
