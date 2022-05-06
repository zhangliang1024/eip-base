package com.eip.common.auth.server.config;

import com.eip.common.auth.server.exception.AuthServerAuthenticationEntryPoint;
import com.eip.common.auth.server.exception.AuthServerWebResponseExceptionTranslator;
import com.eip.common.auth.server.filter.AuthServerClientCredentialsTokenEndpointFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * ClassName: AuthorizationServerConfig
 * Function:  OAuth认证中心配置类
 * 1. 不是所有类都可以做为OAthu2.0认证中心的配置类，必须集成 AuthorizationServerConfigurerAdapter
 * 2. 标注：@EnableAuthorizationServer注解
 * Date: 2021年12月17 18:10:43
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 令牌存储策略
     */
    @Autowired
    private TokenStore tokenStore;
    /**
     * 客户端存储策略，这里使用内存方式，后续可以存储在数据库
     */
    @Autowired
    private ClientDetailsService clientDetailsService;
    /**
     * Security的认证管理器，密码模式需要用到
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private AuthServerAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private DataSource dataSource;
    /**
     * 令牌端点安全约束配置
     * 如：/oauth/token对哪些开放
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //自定义 过滤器处理客户端异常
        AuthServerClientCredentialsTokenEndpointFilter filter =
                new AuthServerClientCredentialsTokenEndpointFilter(security, authenticationEntryPoint);
        filter.afterPropertiesSet();
        security.addTokenEndpointAuthenticationFilter(filter);

        security
                .authenticationEntryPoint(authenticationEntryPoint)
                //开启/oauth/token_key验证端口权限访问
                .tokenKeyAccess("premitAll()")
                //开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("premitAll()");
        //一定不要添加allowFormAuthenticationForClients，否则自定义的AuthServerClientCredentialsTokenEndpointFilter不生效
        //.allowFormAuthenticationForClients();
    }

    /**
     * 客户端详情配置，如：秘钥 唯一ID
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /**
     * 配置令牌访问端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //设置异常WebResponseExceptionTranslator，用于处理用户名，密码错误、授权类型不正确的异常
                .exceptionTranslator(new AuthServerWebResponseExceptionTranslator())
                //授权码模式所需要的authorizationCodeServices
                .authorizationCodeServices(authorizationCodeServices())
                //密码模式所需要的authenticationManager
                .authenticationManager(authenticationManager)
                //令牌管理服务，无论哪种模式都需要
                .tokenServices(tokenServices())
                //只允许POST提交访问令牌，uri：/oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);

    }

    /**
     * 使用授权码模式必须配置一个授权码服务，用来颁布和删除授权码
     * 授权码支持多种方式存储：如内存、数据库等
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    /**
     * 令牌服务配置
     * 除了令牌的存储策略需要配置，还需要配置令牌的AuthorizationServerTokenServices来创建、获取、刷新令牌
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();

        //客户端配置策略
        services.setClientDetailsService(clientDetailsService);
        //支持令牌刷新
        services.setSupportRefreshToken(true);
        //令牌服务
        services.setTokenStore(tokenStore);
        //access_token的过期时间
        services.setAccessTokenValiditySeconds(60 * 60 * 24 * 3);
        //refresh_token的过期时间
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);

        //设置令牌增强，使用JwtAccessTokenConverter进行转换
        services.setTokenEnhancer(jwtAccessTokenConverter);
        return services;
    }

}
