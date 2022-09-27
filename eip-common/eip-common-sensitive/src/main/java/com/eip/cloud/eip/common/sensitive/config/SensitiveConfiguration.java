package com.eip.cloud.eip.common.sensitive.config;

import com.eip.cloud.eip.common.sensitive.resolve.RequestMappingResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * ClassName: SensitiveAutoConfiguration
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@ConditionalOnExpression
public class SensitiveConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RequestMappingResolver handlerMethodServletParser(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new RequestMappingResolver(requestMappingHandlerMapping);
    }
}
