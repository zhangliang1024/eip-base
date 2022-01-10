package com.eip.common.web.config;

import com.eip.common.web.aysnc.AsyncProperties;
import com.eip.common.web.aysnc.WebAsyncConfig;
import com.eip.common.web.feign.FeignDefaultRequestInterceptor;
import com.eip.common.web.feign.FeignProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ClassName: BaseAutoConfiguration
 * Function:
 * Date: 2022年01月10 13:16:02
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class BaseAutoConfiguration {


    /**
     * Feign调用配置
     */
    @Configuration
    @ConditionalOnClass(FeignAutoConfiguration.class)
    @EnableConfigurationProperties(FeignProperties.class)
    @EnableFeignClients(basePackages = "com.eip",defaultConfiguration = FeignDefaultRequestInterceptor.class)
    public static class FeignConfig{}

    /**
     * 当启用@EnableAsync注解时，才会启动自定义线程池
     */
    @Configuration
    @ConditionalOnBean(annotation = EnableAsync.class)
    @EnableConfigurationProperties(AsyncProperties.class)
    @Import(WebAsyncConfig.class)
    public static class AsyncConfig{}

}
