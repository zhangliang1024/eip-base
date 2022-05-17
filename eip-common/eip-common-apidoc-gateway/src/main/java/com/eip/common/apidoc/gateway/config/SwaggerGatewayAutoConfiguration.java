package com.eip.common.apidoc.gateway.config;

import com.eip.common.apidoc.gateway.gateway.GatewaySwaggerProvider;
import com.eip.common.apidoc.gateway.filter.SwaggerHeaderFilter;
import com.eip.common.apidoc.gateway.listener.ApplicationAccessUrlRunner;
import com.eip.common.apidoc.gateway.zuul.ZuulSwaggerProvider;
import com.netflix.zuul.filters.ZuulServletFilter;
import com.netflix.zuul.http.ZuulServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Function: swagger网关自动配置
 * Date: 2022年05月16 20:02:04
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@ComponentScan
@Configuration
@ConditionalOnProperty(name = "eip.gateway.swagger.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerGatewayProperties.class)
public class SwaggerGatewayAutoConfiguration {

    private final SwaggerGatewayProperties swaggerGatewayProperties;

    public SwaggerGatewayAutoConfiguration(SwaggerGatewayProperties swaggerGatewayProperties) {
        this.swaggerGatewayProperties = swaggerGatewayProperties;
    }

    @Bean
    public ApplicationAccessUrlRunner applicationAccessUrlRunner(Environment env){
        return new ApplicationAccessUrlRunner(env);
    }

    @Configuration
    @ConditionalOnClass({ZuulServlet.class, ZuulServletFilter.class})
    public class ZuulSwaggerConfiguration {

        @Bean
        @Primary
        public ZuulSwaggerProvider zuulSwaggerProvider(org.springframework.cloud.netflix.zuul.filters.RouteLocator routeLocator,
                                                       ZuulProperties zuulProperties) {
            return new ZuulSwaggerProvider(routeLocator, zuulProperties, swaggerGatewayProperties);
        }

    }

    @Configuration
    @ConditionalOnClass({WebFluxConfigurer.class})
    public class GatewaySwaggerConfiguration {

        @Bean
        @Primary//标记primary，让网关默认使用自定义provider
        public GatewaySwaggerProvider gatewaySwaggerProvider(org.springframework.cloud.gateway.route.RouteLocator routeLocator,
                                                             GatewayProperties gatewayProperties) {
            return new GatewaySwaggerProvider(routeLocator, gatewayProperties, swaggerGatewayProperties);
        }

        @Bean
        public SwaggerHeaderFilter swaggerHeaderFilter() {
            return new SwaggerHeaderFilter(swaggerGatewayProperties);
        }
    }


}