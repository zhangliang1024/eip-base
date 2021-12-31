package com.eip.common.cache.config;

import com.eip.common.cache.aop.CacheAspectHandler;
import com.eip.common.cache.builder.PowerCacheBuilder;
import com.eip.common.cache.core.guava.GuavaMemoryCacheHandler;
import com.eip.common.cache.core.guava.MemoryCacheHandler;
import com.eip.common.cache.core.redis.ClusterCacheHandler;
import com.eip.common.cache.core.redis.RedisClusterCacheHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Barnett
 * @Date: 2021/11/26 14:17
 * @Version: V1.0.0
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnProperty(prefix = "eip.cache", value = "enabled", havingValue = "true", matchIfMissing = true)
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "eip.cache", value = "memoryType", havingValue = "GUAVA", matchIfMissing = true)
    public ClusterCacheHandler clusterCacheHandler() {
        return new RedisClusterCacheHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "eip.cache", value = "clusteryType", havingValue = "REDIS", matchIfMissing = true)
    public MemoryCacheHandler memoryCacheHandler() {
        return new GuavaMemoryCacheHandler();
    }

    @Bean
    public PowerCacheBuilder powerCacheBuilder() {
        return new PowerCacheBuilder();
    }

    /**
     * 配置aop
     */
    @Bean
    public CacheAspectHandler cacheAopHandler() {
        return new CacheAspectHandler();
    }
}
