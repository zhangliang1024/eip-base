package com.eip.common.auth.gateway.config.security;

import cn.hutool.core.util.ArrayUtil;
import com.eip.common.auth.gateway.exception.RequestAccessDeniedHandler;
import com.eip.common.auth.gateway.exception.RequestAuthenticationEntryPoint;
import com.eip.common.auth.gateway.filter.CorsFilter;
import com.eip.common.auth.gateway.config.property.GatewayProperies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * ClassName: SecurityConfig
 * Function: OAuth2安全配置
 * Date: 2022年01月18 13:29:29
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableWebFluxSecurity
public class AuthSecurityConfig {

    //JWT的权鉴管理器
    @Autowired
    private AuthAccessManager accessManager;

    //token校验管理器
    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    //token过期的异常处理
    @Autowired
    private RequestAuthenticationEntryPoint requestAuthenticationEntryPoint;

    //权限不足异常处理
    @Autowired
    private RequestAccessDeniedHandler requestAccessDeniedHandler;

    @Autowired
    private CorsFilter corsFilter;

    //系统参数配置
    @Autowired
    private GatewayProperies sysConfig;

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        /**认证过滤器放入认证管理器，利用认证管理器进行令牌校验**/
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());

        http.httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                /**白名单直接放行**/
                .pathMatchers(ArrayUtil.toArray(sysConfig.getIgnoreUrls(), String.class)).permitAll()
                .anyExchange().access(accessManager) //其它的请求必须鉴权，使用鉴权管理器
                .and().exceptionHandling() //处理异常情况：认证失败和权限不足
                .authenticationEntryPoint(requestAuthenticationEntryPoint) //认证未通过，不允许访问异常处理器
                .accessDeniedHandler(requestAccessDeniedHandler)  //认证通过，但是没权限处理器
                .and()
                //跨域处理
                .addFilterAt(corsFilter, SecurityWebFiltersOrder.CORS)
                /**token认证过滤器，用于校验token和认证**/
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

}
