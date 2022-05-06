package com.eip.common.gateway.config;

import com.eip.common.gateway.service.DefaultLimiterLevelHandler;
import com.eip.common.gateway.service.LimiterLevelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: GatewayAutoConfiguration
 * Function:
 * Date: 2022年01月19 10:07:20
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class GatewayAutoConfiguration {

    @Bean
    public LimiterLevelResolver limiterLevelResolver(){
        return new DefaultLimiterLevelHandler();
    }

}
