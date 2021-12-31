package com.eip.common.limit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 限流
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLimit {

    /**
     * Key的prefix
     */
    String prefix() default "";

    /**
     * 超时时间单位: 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * Key的分隔符（默认 :
     * 生成的Key：N:SO1008:500
     */
    String delimiter() default ":";

    /**
     * 过期时间
     */
    int expire() default 1;

    /**
     * 最多的访问限制次数
     */
    long count() default 20;

    /**
     * 默认异常信息
     */
    String message() default "You have been dragged into the blacklist";

    /**
     * 限流描述
     */
    String description() default "";


}
