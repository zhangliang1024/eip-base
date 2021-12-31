package com.eip.common.limit.annotation;


import java.lang.annotation.*;

/**
 * 缓存 Key 的参数
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisParam {

    /**
     * 字段名称
     */
    String name() default "";
}