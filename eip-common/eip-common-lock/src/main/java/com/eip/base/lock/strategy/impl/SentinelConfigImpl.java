package com.eip.base.lock.strategy.impl;

import com.eip.base.lock.config.RedissonProperties;
import com.eip.base.lock.strategy.RedissonConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * 哨兵集群部署Redis连接配置
 */
@Slf4j
public class SentinelConfigImpl implements RedissonConfigService {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String sentinelAliasName = addrTokens[0];
            //设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinelAliasName);
            config.useSentinelServers().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSentinelServers().setPassword(password);
            }
            //设置sentinel节点的服务IP和端口
            for (int i = 1; i < addrTokens.length; i++) {
                config.useSentinelServers().addSentinelAddress(REDIS_CONNECTION_PREFIX + addrTokens[i]);
            }
            log.info("[lock - sentinel] init success ...");
        } catch (Exception e) {
            log.error("[lock - sentinel] init fail ...", e);
            throw e;
        }
        return config;
    }
}
