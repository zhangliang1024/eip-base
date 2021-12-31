package com.eip.common.limit.generator;


import org.aspectj.lang.ProceedingJoinPoint;

/**
 * key生成器
 */
public interface RedisKeyGenerator {


    /**
     * 获取AOP参数,生成指定缓存Key
     *
     * @param prefix    key前缀
     * @param delimiter 分隔符
     * @param pjp       PJP
     * @return 缓存KEY
     */
    String generate(String prefix, String delimiter, ProceedingJoinPoint pjp);
}