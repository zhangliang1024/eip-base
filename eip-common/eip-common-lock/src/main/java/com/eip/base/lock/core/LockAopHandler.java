package com.eip.base.lock.core;

import com.eip.base.lock.annotation.CLock;
import com.eip.base.lock.core.RedissonLock;
import com.eip.base.lock.eums.LockMode;
import com.eip.base.lock.utils.SpelExpressionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 11:06
 * @Version: V1.0.0
 * @Description:
 *  1. Redisson分布式锁注解解析器
 *  2. order越小优先级越高 为了在事务提交之后 再释放分布式锁
 */
@Slf4j
@Aspect
@Order(1)
public class LockAopHandler {


    @Autowired
    private RedissonLock redissonLock;

    @Around("@annotation(com.eip.base.lock.annotation.CLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("[lock] - Aop执行获取分布式锁");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CLock lock = method.getAnnotation(CLock.class);

        //解析分布式锁的key值
        String key = SpelExpressionUtils.parserSpel(method, joinPoint.getArgs(), lock.key(), String.class, null);
        //获取超时时间，默认10秒
        long leaseTime = lock.leaseTime();
        try {
            //判断是否为手动加锁
            if (lock.lockMode() == LockMode.AUTO && !StringUtils.isBlank(key)) {
                //设置分布式锁
                redissonLock.lockTime(key, leaseTime, TimeUnit.SECONDS);
            }
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            //判断该线程是否持有当前锁
            if (redissonLock.isHoldLock(key)) {
                redissonLock.unlock();
            }
        }
    }
}
