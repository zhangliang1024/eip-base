package com.eip.common.auth.oauth.server;

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
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * ClassName: AuthorizationServerConfig
 * Function:  OAuth认证中心配置类
 *    1. 不是所有类都可以做为OAthu2.0认证中心的配置类，必须集成 AuthorizationServerConfigurerAdapter
 *    2. 标注：@EnableAuthorizationServer注解
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



    /**
     * 令牌端点安全约束配置，如：/oauth/token对哪些开放
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("premitAll()")
                .checkTokenAccess("premitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 客户端详情配置，如：秘钥 唯一ID
     * TODO 测试暂存 内存
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //客户端ID
                .withClient("client")
                //客户端秘钥
                .secret(new BCryptPasswordEncoder().encode("123"))
                //资源ID 唯一。如：订单服务做为一个资源 可以设置多个
                .resourceIds("res")
                //授权模式 共四种：1.authorization_code（授权码模式） 2.password（密码模式） 3.client_credentials（客户端模式） 4. implicit（简化模式）
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                //允许的授权范围
                .scopes("all")
                .autoApprove(false)
                .redirectUris("http://www.baidu.com");
    }

    /**
     * 令牌访问端点配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authorizationCodeServices(authorizationCodeServices())
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);

    }

    /**
     * 使用授权码模式必须配置一个授权码服务，用来颁布和删除授权码
     * 授权码支持多种方式存储：如内存、数据库等
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        //TODO 测试暂存内存
        return new InMemoryAuthorizationCodeServices();
    }


    /**
     * 令牌服务配置
     * 除了令牌的存储策略需要配置，还需要配置令牌的AuthorizationServerTokenServices来创建、获取、刷新令牌
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();

        //客户端配置策略
        services.setClientDetailsService(clientDetailsService);
        //支持令牌刷新
        services.setSupportRefreshToken(true);
        //令牌服务
        services.setTokenStore(tokenStore);
        //access_token过期时间
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        //refresh_token过期时间
        services.setRefreshTokenValiditySeconds(60 * 60 * 2 );
        return services;
    }

}
