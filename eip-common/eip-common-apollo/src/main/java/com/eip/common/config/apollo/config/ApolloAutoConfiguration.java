package com.eip.common.config.apollo.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.eip.common.config.apollo.listener.ApolloConfigListener;
import com.eip.common.config.apollo.service.ApolloDencryptService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ApolloAutoConfiguration
 * Function:
 * Date: 2022年01月10 17:03:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableApolloConfig
public class ApolloAutoConfiguration {

    @Bean
    public ApolloConfigListener apolloConfigListener(){
        return new ApolloConfigListener();
    }

}
