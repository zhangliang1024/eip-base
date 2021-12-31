package com.eip.common.limit.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 基于 Redis 实现的分布式限流
 */
public class RedisLimitHandler implements LimitHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisLimitHandler.class);

    @Autowired
    private RedisTemplate<String, Serializable> redisLimitTemplate;

    /**
     * 尝试获取
     *
     * @param key         key
     * @param limitCount  限流数
     * @param expire      过期时间
     * @param timeUnit    单位: 秒
     * @param description 描述信息
     * @return true | false
     */
    @Override
    public boolean tryAcquire(String key, long limitCount, String description, long expire, TimeUnit timeUnit) {
        final List<String> keys = Collections.singletonList(key);
        final long limitExpire = Expiration.from(expire, timeUnit).getExpirationTimeInSeconds();
        String luaScript = buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        Number count = redisLimitTemplate.execute(redisScript, keys, limitCount, limitExpire);
        if (log.isDebugEnabled()) {
            log.info("Access try count is {} for description={} and key = {}", count, description, key);
        }
        return count != null && count.longValue() <= limitCount;
    }


    /**
     * 限流 脚本
     *
     * @return lua脚本
     */
    private String buildLuaScript() {
        // 调用不超过最大值，则直接返回
        // 执行计算器自加
        // 从第一次调用开始限流，设置对应键值的过期
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }


}