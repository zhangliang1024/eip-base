package com.eip.common.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 16:57
 * @Version: V1.0.0
 * @Description: 添加缓存注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CachePut {

    /**
     * 缓存名称
     */
    String key();
    /**
     * 缓存过期时间，默认：300s
     */
    long timeout() default 300;
    /**
     * 是否随机缓存过期时间，默认不随机。一旦指定该属性，则timeout失效
     */
    boolean isRandomTimeout() default false;
    /**
     * 最大随机时间，isRandomTimeout为true时生效
     */
    long maxTimeout() default 300;
    /**
     * 最小的随机时间，isRandomTimeout为true时生效
     */
    long minTimeout() default 100;
    /**
     * 缓存相关时间单位，默认为秒
     */
    TimeUnit unit() default TimeUnit.SECONDS;
    /**
     * 缓存包含的条件，符合条件的会被缓存
     */
    String condition() default "";
    /**
     * 缓存包含的排除条件，符合条件的不会被缓存，优先级高于condition
     * @return
     */
    String unless() default "";
    /**
     * 是否同步保存缓存，默认异步
     * @return
     */
    boolean isSync() default false;



}
