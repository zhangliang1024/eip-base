package com.eip.base.lock.strategy;

import com.eip.base.lock.config.RedissonProperties;
import org.redisson.config.Config;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 11:06
 * @Version: V1.0.0
 * @Description: Redisson config初始化
 */
public interface RedissonConfigService {

    /**
     * Redis地址配置前缀
     */
    String REDIS_CONNECTION_PREFIX = "redis://";

    /**
     * Description: 根据不同的Redis配置策略创建对应的Config
     *
     * @auther: Barnett
     * @date: 2021/11/24 11:19
     * @param:
     * @return:
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);
}
