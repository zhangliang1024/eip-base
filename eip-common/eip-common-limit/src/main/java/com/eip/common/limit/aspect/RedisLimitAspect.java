package com.eip.common.limit.aspect;

import com.eip.common.limit.annotation.RedisLimit;
import com.eip.common.limit.generator.RedisKeyGenerator;
import com.eip.common.limit.handler.LimitHandler;
import com.eip.common.limit.handler.RedisLimitHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * 限流AOP拦截实现
 */
@Aspect
public class RedisLimitAspect {
    private static final Logger log = LoggerFactory.getLogger(RedisLimitHandler.class);

    @Autowired
    private LimitHandler limitHandler;
    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    @Around("execution(public * *(..)) && @annotation(com.eip.common.limit.annotation.RedisLimit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RedisLimit limitAnnotation = method.getAnnotation(RedisLimit.class);
        //获取限流配置
        final String prefix = limitAnnotation.prefix();
        final String delimiter = limitAnnotation.delimiter();
        final String description = limitAnnotation.description();
        final long count = limitAnnotation.count();
        final long limitExpire = limitAnnotation.expire();
        final TimeUnit timeUnit = limitAnnotation.timeUnit();
        //生成唯一KEY
        String key = redisKeyGenerator.generate(prefix, delimiter, pjp);
        try {
            final boolean acquire = this.limitHandler.tryAcquire(key, count, description,limitExpire,timeUnit);
            if (acquire) {
                return pjp.proceed();
            } else {
                throw new RuntimeException(limitAnnotation.message());
            }
        } catch (Throwable e) {
            log.error("[server exception]", e);
            if (e instanceof RuntimeException) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
            throw new RuntimeException("server exception");
        }
    }


}