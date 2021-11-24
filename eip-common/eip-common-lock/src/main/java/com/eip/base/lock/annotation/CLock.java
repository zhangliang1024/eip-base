package com.eip.base.lock.annotation;

import com.eip.base.lock.eums.LockMode;
import java.lang.annotation.*;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 11:06
 * @Version: V1.0.0
 * @Description: 基于注解的分布式式锁
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CLock {

    /**
     * 锁的名称
     */
    String key() default "clock";

    /**
     * 锁的有效时间
     */
    long leaseTime() default 10L;

    /**
     * 上锁的模式 - 自动/手动
     */
    LockMode lockMode() default LockMode.AUTO;
}


