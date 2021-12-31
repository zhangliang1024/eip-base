package com.eip.common.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 17:17
 * @Version: V1.0.0
 * @Description: 获取缓存
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheGet {

    /**
     * 缓存的key
     */
    String key();
    /**
     * 缓存的过期时间，默认300秒
     */
    long timeout() default 300;
    /**
     * 是否随机缓存过期时间，默认不随机，一旦指定该属性，则timeout会失效
     */
    boolean isRandomTimeout() default false;
    /**
     * 最大的随机时间，isRandomTimeout为true时生效
     */
    long maxTimeout() default 300;
    /**
     * 最小的随机时间，isRandomTimeout为true时生效
     */
    long minTimeout() default 100;
    /**
     * 缓存的包含条件（符合该条件的数据会从缓存中查询）
     */
    String condition() default "";
    /**
     * 缓存的排除条件（符合该条件的数据不会从缓存中查询，优先级高于condition）
     */
    String unless() default "";
    /**
     * 缓存相关时间单位，默认为秒
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}