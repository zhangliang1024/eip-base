package com.eip.common.idempotent.config;

import com.eip.common.idempotent.interceptor.AutoIdemInterceptor;
import com.eip.common.idempotent.interceptor.WebConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: AutoIdempotentConfiguration
 * Function:
 * Date: 2021年12月07 16:52:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Configuration
@ComponentScan("com.eip.common.idempotent")
@ConditionalOnWebApplication
@EnableConfigurationProperties(AutoIdemProperties.class)
@ConditionalOnProperty(prefix = "eip.auto-idem", name = "true", havingValue = "true", matchIfMissing = true)
public class AutoIdemConfiguration {

    @Autowired
    private AutoIdemProperties idemProperties;

    @Bean
    public AutoIdemInterceptor autoIdempotentInterceptor() {
        return new AutoIdemInterceptor();
    }

    @Bean
    public WebConfiguration webConfiguration() {
        log.info("[eip-idem] - 自动幂等拦截[启用] ");
        log.info("[eip-idem] - 幂等token失效时间[{}ms] ", idemProperties.getExpireTime());
        return new WebConfiguration();
    }

}
