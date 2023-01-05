package com.eip.common.sms.phone;

import com.eip.common.sms.phone.component.AliyunSmsHelper;
import com.eip.common.sms.phone.config.AliyunSmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类
 *
 * @author yinzl
 */
@Configuration
@ConditionalOnProperty(prefix = "eip.sms.aliyun", name = "access-key-id")
@EnableConfigurationProperties(AliyunSmsProperties.class)
@ComponentScan(basePackages = "com.eip.common.sms.phone")
public class AliyunSmsAutoConfiguration {

    private final AliyunSmsProperties aliyunSmsProperties;

    public AliyunSmsAutoConfiguration(final AliyunSmsProperties aliyunSmsProperties) {
        this.aliyunSmsProperties = aliyunSmsProperties;
    }

    /**
     * 阿里云短信辅助工具
     */
    @Bean
    public AliyunSmsHelper aliyunSmsHelper() {
        final AliyunSmsHelper helper = new AliyunSmsHelper(aliyunSmsProperties);
        helper.init();
        return helper;
    }
}
