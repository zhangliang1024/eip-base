package com.eip.common.thread.config;


import com.eip.common.thread.core.DynamicThreadPoolManager;
import com.eip.common.thread.endpoint.ThreadPoolEndpoint;
import com.eip.common.thread.listener.ApolloConfigUpdateListener;
import com.eip.common.thread.listener.NacosConfigUpdateListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 配置注入
 */
@EnableConfigurationProperties(DynamicThreadPoolProperties.class)
public class DynamicThreadPoolAutoConfiguration {

    @Bean
    public DynamicThreadPoolManager dynamicThreadPoolManager() {
        return new DynamicThreadPoolManager();
    }

    @Bean
    public ThreadPoolEndpoint threadPoolEndpoint() {
        return new ThreadPoolEndpoint();
    }

    @Configuration
    @ConditionalOnClass(value = com.alibaba.nacos.api.config.ConfigService.class)
    protected static class NacosConfiguration {
        @Bean
        public NacosConfigUpdateListener nacosConfigUpdateListener() {
            return new NacosConfigUpdateListener();
        }
    }

    @Configuration
    @ConditionalOnClass(value = com.ctrip.framework.apollo.ConfigService.class)
    protected static class ApolloConfiguration {
        @Bean
        public ApolloConfigUpdateListener apolloConfigUpdateListener() {
            return new ApolloConfigUpdateListener();
        }
    }

}
