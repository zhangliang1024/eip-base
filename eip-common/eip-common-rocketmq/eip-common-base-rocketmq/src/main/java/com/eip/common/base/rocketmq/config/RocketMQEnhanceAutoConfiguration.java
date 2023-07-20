package com.eip.common.base.rocketmq.config;

import com.eip.common.base.rocketmq.template.RocketMQEnhanceTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.List;

/**
 * ClassName: RocketMQEnhanceAutoConfiguration
 * Function: 基础配置类
 * Date: 2023年07月20 15:14:10
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableConfigurationProperties(RocketEnhanceProperties.class)
public class RocketMQEnhanceAutoConfiguration {

    /**
     * 注入增强的RocketMQEnhanceTemplate
     */
    @Bean
    public RocketMQEnhanceTemplate rocketMQEnhanceTemplate(RocketMQTemplate rocketMQTemplate) {
        return new RocketMQEnhanceTemplate(rocketMQTemplate);
    }

    /**
     * 解决RocketMQ Jackson不支持Java时间类型配置
     * 源码参考：{@link org.apache.rocketmq.spring.autoconfigure.MessageConverterConfiguration}
     */
    @Bean
    @Primary
    public RocketMQMessageConverter enhanceRocketMQMessageConverter() {
        RocketMQMessageConverter converter = new RocketMQMessageConverter();
        CompositeMessageConverter compositeMessageConverter = (CompositeMessageConverter) converter.getMessageConverter();
        List<MessageConverter> messageConverterList = compositeMessageConverter.getConverters();
        for (MessageConverter messageConverter : messageConverterList) {
            if (messageConverter instanceof MappingJackson2MessageConverter) {
                MappingJackson2MessageConverter jackson2MessageConverter = (MappingJackson2MessageConverter) messageConverter;
                ObjectMapper objectMapper = jackson2MessageConverter.getObjectMapper();
                objectMapper.registerModules(new JavaTimeModule());
            }
        }
        return converter;
    }

    /**
     * 环境隔离配置
     */
    @Bean
    @ConditionalOnProperty(name = "rocketmq.enhance.enabledIsolation", havingValue = "true")
    public EnvironmentIsolationConfig environmentSetup(RocketEnhanceProperties rocketEnhanceProperties) {
        return new EnvironmentIsolationConfig(rocketEnhanceProperties);
    }
}