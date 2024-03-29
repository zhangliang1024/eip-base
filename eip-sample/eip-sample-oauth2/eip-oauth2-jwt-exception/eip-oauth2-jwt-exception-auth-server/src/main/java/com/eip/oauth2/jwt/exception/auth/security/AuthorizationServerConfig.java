package com.eip.oauth2.jwt.exception.auth.security;

import com.eip.oauth2.jwt.exception.auth.exception.AuthServerWebResponseExceptionTranslator;
import com.eip.oauth2.jwt.exception.auth.filter.AuthServerClientCredentialsTokenEndpointFilter;
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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: AuthorizationServerConfig
 * Function: 认证中心相关配置
 * Date: 2022年01月18 09:56:03
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 令牌端点安全约束配置，如：/oauth/token对哪些开放
     * 主要对一些端点的权限进行配置
     *
     * 配置资源服务器向认证服务器请求验证token的规则，过来验证token有效性的请求，必须是经过身份验证的
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 自定义 用于处理客户端id和密码错误的异常
        AuthServerClientCredentialsTokenEndpointFilter endpointFilter = new AuthServerClientCredentialsTokenEndpointFilter(security, authenticationEntryPoint);
        endpointFilter.afterPropertiesSet();
        security.addTokenEndpointAuthenticationFilter(endpointFilter);

        security
                //开启 oauth/token_key验证端口访问权限
                .tokenKeyAccess("permitAll()")
                //开启 oauth/check_token验证端口认证权限访问
                .checkTokenAccess("permitAll()");
                //一定不要添加allowFormAuthenticationForClients，否则自定义的AuthServerClientCredentialsTokenEndpointFilter不生效
                //表示支持客户端 以表单方式发送 验证token的请求 必须携带client_id和client_secret 做登录认证
                //.allowFormAuthenticationForClients();
    }

    /**
     * 并不是所有的客户端都有权限向认证中心申请令牌的，首先认证中心要知道你是谁，有什么资格
     * 因此，一些必要的配置是认证中心分配给你的，如：客户端唯一ID、秘钥、权限等
     * 客户端配置也支持多种存储方式，如：内存、数据库等
     * 客户端详情配置，如：秘钥 唯一ID
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("myjszl")//客户端ID
                .secret(new BCryptPasswordEncoder().encode("123"))//客户端秘钥
                .resourceIds("res1")//资源ID 唯一，如订单服务作为一个资源，可以设置多个
                //授权模式，共四种：1.authorization_code（授权码模式） 2.password（密码模式） 3.client_credentials（客户端模式）4.implicit（简化模式）
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit",
                        "refresh_token")//支持授权模式
                .scopes("all")//允许的授权范围，客户端的权限。这里的all只是一种标识，可以自定义，为后续的资源服务进行权限控制
                .autoApprove(false) //false 则跳转到授权页面
                .redirectUris("http://www.baidu.com");//授权码模式回调地址
    }

    /**
     * 令牌访问端点配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 设置异常翻译器，用于用户名、密码、授权类型错误提示
                .exceptionTranslator(new AuthServerWebResponseExceptionTranslator())
                //授权码模式所需要的authorizationCodeServices
                .authorizationCodeServices(authorizationCodeServices())
                //密码模式需要的authenticationManager
                .authenticationManager(authenticationManager)
                //令牌管理服务，无论哪种模式都需要
                .tokenServices(tokenServices())
                //只允许POST提交访问令牌 uri: /oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);

    }

    /**
     * 使用授权码模式必须配置一个授权码服务，用来颁布和删除授权码。授权码支持多种存储方式：如内存、数据库等
     * 授权码模式的service，使用授权码模式：authorization_code必须注入
     * TODO 这里演示使用内存存储
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }


    /**
     * Security的认证管理器，密码模式需要用到
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 配置令牌的服务，用来创建、获取、刷新令牌
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        //客户端 配置策略
        services.setClientDetailsService(clientDetailsService);
        //支持令牌的刷新
        services.setSupportRefreshToken(true);
        //令牌服务
        services.setTokenStore(tokenStore);
        //access_token过期时间
        services.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(2));
        //refresh_token过期时间
        services.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        //设置令牌增强，使用JwtAccessTokenConverter进行转换
        services.setTokenEnhancer(jwtAccessTokenConverter);
        return services;
    }


}
