package com.eip.ability.auth.oauth2.config;

import com.eip.ability.auth.oauth2.exception.AuthClientCredentialsTokenEndpointFilter;
import com.eip.ability.auth.oauth2.exception.AuthServerAuthenticationEntryPoint;
import com.eip.ability.auth.oauth2.exception.AuthWebResponseExceptionTranslator;
import com.eip.ability.auth.oauth2.service.ClientDetailsServiceImpl;
import com.eip.common.auth.core.domain.UserInfoDetails;
import com.eip.common.core.constants.AuthConstants;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: AuthorizationServerConfig
 * Function: 授权认证服务器
 * Date: 2022年09月19 18:54:27
 * OAuth2Authserver
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */

@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthServerAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailsServiceImpl clientDetailsService;
    private final TokenStore tokenStore;
    private final JwtAccessTokenConverter jwtTokenEnhancer;


    /**
     * 资源服务器进行校验时，是否需要校验资源服务
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 用于处理客户端id和密码错误的异常
        AuthClientCredentialsTokenEndpointFilter endpointFilter = new AuthClientCredentialsTokenEndpointFilter(security, authenticationEntryPoint);
        endpointFilter.afterPropertiesSet();
        security.addTokenEndpointAuthenticationFilter(endpointFilter);

        security.authenticationEntryPoint(authenticationEntryPoint)
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 用户认证校验
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenServices(tokenServices())
                .authenticationManager(authenticationManager) //对于密码授权模式，提供 AuthenticationManager 用于用户信息的认证
                .exceptionTranslator(new AuthWebResponseExceptionTranslator())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);

    }

    /**
     * 客户端校验存储
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 配置令牌的服务，用来创建、获取、刷新令牌
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setTokenStore(tokenStore); // 令牌服务
        services.setSupportRefreshToken(true); // 支持令牌的刷新
        services.setAccessTokenValiditySeconds(AuthConstants.ACCESS_TOKEN_VALIDITY_SECONDS);
        services.setRefreshTokenValiditySeconds(AuthConstants.REFRESH_TOKEN_VALIDITY_SECONDS);
        services.setTokenEnhancer(jwtTokenEnhancer);
        return services;
    }


}
