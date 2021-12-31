package com.eip.common.cache.aop;

import com.eip.common.cache.utils.SpelExpressionUtils;
import com.eip.common.cache.annotation.CacheDelete;
import com.eip.common.cache.annotation.CacheGet;
import com.eip.common.cache.core.guava.MemoryCacheHandler;
import com.eip.common.cache.core.redis.ClusterCacheHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/26 14:14
 * @Version: V1.0.0
 * @Description: 处理缓存AOP
 */
@Slf4j
@Aspect
public class CacheAspectHandler {

    /**
     * 二级缓存实现
     */
    @Autowired
    private ClusterCacheHandler clusterCacheHandler;
    /**
     * 内存缓存实现
     */
    @Autowired
    private MemoryCacheHandler memoryCacheHandler;


    @Around("@annotation(com.eip.common.cache.annotation.CacheGet)")
    public Object cacheGetAop(ProceedingJoinPoint joinPoint) {
        //获取请求方法
        Method method = getMethod(joinPoint);
        //获取方法参数
        Object[] params = joinPoint.getArgs();
        //获取方法注解
        CacheGet cacheGet = method.getAnnotation(CacheGet.class);

        //获取排除条件
        String unless = cacheGet.unless();
        Boolean unlessFlag = SpelExpressionUtils.parserSpel(method, params, unless, Boolean.class, false);
        //获取包含条件
        String condition = cacheGet.condition();
        Boolean conditionFlag = SpelExpressionUtils.parserSpel(method, params, condition, Boolean.class, true);

        //获取过期时间
        Long timeout = getTimeout(cacheGet);
        TimeUnit unit = cacheGet.unit();
        //目标方法返回值
        Object value = null;

        //如果unless为false，并且condition为true时，才会进入缓存读取，否则直接执行目标方法
        if (!unlessFlag && conditionFlag) {

            String key = cacheGet.key();
            //解析Key中的spel表达式
            key = SpelExpressionUtils.parserSpel(method, params, key, String.class, null);

            //从缓存中获取数据
            value = getCache(key, timeout, unit);

            //从分布式缓存获取数据为空
            if (Objects.isNull(value)) {
                //加锁访问后端数据
                synchronized (key.intern()) {
                    //双重锁判断
                    value = getCache(key, timeout, unit);
                    if (Objects.isNull(value)) {
                        //直接调用目标方法
                        try {
                            value = joinPoint.proceed();
                            if (Objects.isNull(value)) {
                                return value;
                            }
                            //建立缓存
                            memoryCacheHandler.set(key, value, timeout, unit);
                            clusterCacheHandler.set(key, value, timeout, unit);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            try {
                value = joinPoint.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    @Around("@annotation(com.eip.common.cache.annotation.CacheDelete)")
    public Object cacheDeleteAop(ProceedingJoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        Object[] params = joinPoint.getArgs();
        CacheDelete cacheDelete = method.getAnnotation(CacheDelete.class);

        //是否全部清除
        boolean deleteAll = cacheDelete.isDeleteAll();
        //是否前置删除
        boolean flag = cacheDelete.beforeInvocation();

        //获取排除条件
        String unless = cacheDelete.unless();
        Boolean unlessFlag = SpelExpressionUtils.parserSpel(method, params, unless, Boolean.class, false);
        //获取包含条件
        String condition = cacheDelete.condition();
        Boolean conditionFlag = SpelExpressionUtils.parserSpel(method, params, condition, Boolean.class, true);

        //获取key
        String[] keys = cacheDelete.key();

        //解析key中的spel表达式
        List<String> keysList = new ArrayList<>();
        for (String key : keys) {
            String k = SpelExpressionUtils.parserSpel(method, params, key, String.class, null);
            keysList.add(k);
        }

        //前置删除
        if (flag && !unlessFlag && conditionFlag) {
            for (String key : keysList) {
                memoryCacheHandler.remove(key);
                //调用方法删除其他节点缓存
                memoryCacheHandler.remove(key);
                clusterCacheHandler.remove(key);
            }
        }

        //调用目标方法
        try {
            Object result = joinPoint.proceed();
            //后置删除
            if (!flag && !unlessFlag && conditionFlag) {
                for (String key : keysList) {
                    memoryCacheHandler.remove(key);
                    //调用方法删除其他节点缓存
                    memoryCacheHandler.remove(key);
                    clusterCacheHandler.remove(key);
                }
            }
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 从缓存中获取数据
     */
    private Object getCache(String key, long timeout, TimeUnit unit) {
        //从内存中获取数据
        Object value = memoryCacheHandler.get(key, timeout);
        //若内存缓存为空
        if (Objects.isNull(value)) {
            //从分布式缓存中获取数据
            value = clusterCacheHandler.get(key, timeout);

            if (!Objects.isNull(value)) {
                //重建内存缓存
                memoryCacheHandler.set(key, value, timeout, unit);
            }
        }
        return value;
    }


    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        return method;
    }


    private Long getTimeout(CacheGet cacheGet) {
        boolean randomTimeout = cacheGet.isRandomTimeout();
        //如果设置了随机过期
        if (randomTimeout) {
            //最小过期时间
            long min = cacheGet.minTimeout();
            //最大过期时间
            long max = cacheGet.maxTimeout();
            return (long) (Math.random() * (max - min)) + min;
        } else {
            //若没设置则返回过期时间
            return cacheGet.timeout();
        }
    }


}
