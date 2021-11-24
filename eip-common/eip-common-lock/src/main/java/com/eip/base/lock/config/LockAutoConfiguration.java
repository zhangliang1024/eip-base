package com.eip.base.lock.config;

import com.eip.base.lock.core.LockAopHandler;
import com.eip.base.lock.core.RedissonLock;
import com.eip.base.lock.core.RedissonManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 11:06
 * @Version: V1.0.0
 * @Description: Redisson自动化配置
 */
@Slf4j
@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class LockAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @Order(value = 2)
    public RedissonLock redissonLock(RedissonManager redissonManager) {
        RedissonLock redissonLock = new RedissonLock(redissonManager);
        log.info("[Lock - redisson] init ...");
        return redissonLock;
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 1)
    public RedissonManager redissonManager(RedissonProperties redissonProperties) {
        RedissonManager redissonManager = new RedissonManager(redissonProperties);
        log.info("[Lock - manager] init , type - {} address - {}" ,redissonProperties.getType(),redissonProperties.getAddress());
        return redissonManager;
    }

    @Bean
    public LockAopHandler distributedLockHandler(){
        return new LockAopHandler();
    }
}

