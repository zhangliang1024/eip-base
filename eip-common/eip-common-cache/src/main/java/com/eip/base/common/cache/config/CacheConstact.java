package com.eip.base.common.cache.config;

/**
 * @Author: Barnett
 * @Date: 2021/11/26 17:50
 * @Version: V1.0.0
 * @Description:
 */
public class CacheConstact {

    /**
     * 缓存最大容量
     */
    public static final int CACHE_CONCURRENCY_LEVEL = 10;
    /**
     * 缓存最大容量
     */
    public static final long CACHE_MAX_SIZE = 100L;
    /**
     * 默认缓存时间: 300s
     */
    public static final long DEFAULT_CACHE_SEONDS = 300L;
    /**
     * 默认缓存时间: 30s
     */
    public static final long DEFAULT_CACHE_MIN_SEONDS = 30L;
    /**
     * 默认不做缓存
     */
    public static final long DEFAULT_FOREVER_CACHE = -1L;
    /**
     * 默认缓存版本
     */
    public static final String CACHE_VERSION_KEY = "V:0";

    /**
     * 内存缓存类型枚举
     */
    public enum Memory {
        GUAVA;
    }

    /**
     * 分布式缓存类型枚举
     */
    public enum Cluster {
        REDIS;
    }

}
