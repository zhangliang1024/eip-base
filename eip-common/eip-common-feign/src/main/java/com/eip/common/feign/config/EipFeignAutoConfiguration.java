package com.eip.common.feign.config;

import com.eip.common.feign.FeignProperties;
import com.eip.common.feign.FeignRequestInterceptor;
import com.eip.common.feign.annotation.EipEnableFeignClients;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: FeignAutoConfiguration
 * Function:
 * Date: 2022年01月17 14:43:52
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class EipFeignAutoConfiguration {


    /**
     * Feign调用配置
     */
    @Configuration
    @ConditionalOnClass(FeignAutoConfiguration.class)
    @EnableConfigurationProperties(FeignProperties.class)
    @EipEnableFeignClients(basePackages = "com.eip.business.feign",defaultConfiguration = FeignRequestInterceptor.class)
    public static class FeignConfig{}

}
