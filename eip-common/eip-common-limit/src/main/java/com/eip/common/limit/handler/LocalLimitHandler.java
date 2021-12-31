package com.eip.common.limit.handler;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Local 实现的本地限流
 */
public class LocalLimitHandler implements LimitHandler {

    private static final Logger log = LoggerFactory.getLogger(LocalLimitHandler.class);

    private Map<String, RateLimiter> localLimiterMap = Maps.newConcurrentMap();

    /**
     * 尝试获取
     *
     * @param key         key
     * @param count       限流数
     * @param expire      超时时间
     * @param timeUnit    单位: 秒
     * @param description 描述信息
     * @return true | false
     */
    @Override
    public boolean tryAcquire(String key, long count, String description, long expire, TimeUnit timeUnit) {
        final long limitExpire = Expiration.from(expire, timeUnit).getExpirationTimeInSeconds();
        RateLimiter rateLimiter = getRateLimiter(key,count,limitExpire);
        //获取一个令牌，等待10毫秒: 指定超时时间，一旦判断在timeout时间内无法获取令牌，返回FALSE
        boolean access = rateLimiter.tryAcquire(1, 20, TimeUnit.MILLISECONDS);
        if (log.isDebugEnabled()) {
            log.info("Access try count is {} for description={} and key = {}", count, description, key);
        }
        return access;
    }

    /**
     * 不同的key执行不同的限流策略
     *
     * @param key         key
     * @param count       限流数
     * @param limitExpire 限流时间
     * @return
     */
    private RateLimiter getRateLimiter(String key,long count,long limitExpire) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("limit local key is not allow  empty");
        }
        //计算每秒允许的请求数
        Double limitNum = Double.valueOf(String.valueOf(count));
        Double permitsPerSecond = limitNum / limitExpire;

        RateLimiter rateLimiter = localLimiterMap.get(key);
        if (rateLimiter == null) {
            RateLimiter newRateLimiter = RateLimiter.create(permitsPerSecond);
            rateLimiter = localLimiterMap.putIfAbsent(key, newRateLimiter);
            if (rateLimiter == null) {
                rateLimiter = newRateLimiter;
            }
        }else {
            if(rateLimiter.getRate() != permitsPerSecond){
                RateLimiter newRateLimiter = RateLimiter.create(permitsPerSecond);
                rateLimiter = localLimiterMap.put(key, newRateLimiter);
                if (rateLimiter == null) {
                    rateLimiter = newRateLimiter;
                }
            }
        }
        return rateLimiter;
    }
}
