package com.eip.base.common.cache.core;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 17:28
 * @Tersion: T1.0.0
 * @Description: 缓存操作规范
 */
public interface BaseCacheHandler {


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    <T extends Object> T get(String key);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    <T extends Object> T get(String key, Long expireTime);

    /**
     * 添加缓存
     *
     * @param key
     * @param Talue
     * @return
     */
    <T extends Object> boolean set(String key, T Talue);

    /**
     * 添加缓存且携带过期时间
     *
     * @param key
     * @param Talue
     * @param timeOut
     * @param unit
     * @return
     */
    <T extends Object> boolean set(String key, T Talue, Long timeOut, TimeUnit unit);

    /**
     * 移除缓存
     *
     * @param key
     * @return
     */
    boolean remove(String key);

    /**
     * 移除缓存
     *
     * @param key
     * @return
     */
    boolean remove(String key, Long timeOut);

    /**
     * 缓存是否存在
     *
     * @param:
     * @return:
     */
    boolean contains(String key);
}
