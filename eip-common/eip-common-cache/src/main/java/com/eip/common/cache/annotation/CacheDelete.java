package com.eip.common.cache.annotation;

import java.lang.annotation.*;

/**
 * @Author: Barnett
 * @Date: 2021/11/25 17:03
 * @Version: V1.0.0
 * @Description: 删除缓存
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheDelete {

    /**
     * 需要删除的缓存keys
     */
    String[] key() default "";
    /**
     * 是否删除苏所有缓存，key参数会失效
     */
    boolean isDeleteAll() default false;
    /**
     * 删除缓存的包含条件(符合条件的会从缓存中删除)
     */
    String condition() default "";
    /**
     * 删除缓存的排除条件(符合该条件的数据不会删除，优先级高于condition)
     */
    String unless() default "";
    /**
     * 是否为前置删除(在业务执行前删除缓存，默认为后置删除)
     */
    boolean beforeInvocation() default false;

}
