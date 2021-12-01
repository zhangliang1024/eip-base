package com.eip.base.common.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Barnett
 * @Date: 2021/11/26 17:51
 * @Version: V1.0.0
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "eip.cache")
public class CacheProperties {

    /**
     * 是否开启缓存
     */
    boolean enabled;
    /**
     * 一级缓存类型枚举
      */
    CacheConstact.Memory memoryType;
    /**
     * 二级缓存类型
     */
    CacheConstact.Cluster clusterType;
    /**
     * 缓存最大容量
     */
    public long cacheMaxSize = CacheConstact.CACHE_MAX_SIZE;
    /**
     * 默认缓存时间: 300s
     */
    public long cacheSeonds = CacheConstact.DEFAULT_CACHE_SEONDS;
    /**
     * 默认缓存版本
     */
    public String cacheVersionKey = CacheConstact.CACHE_VERSION_KEY;

}
