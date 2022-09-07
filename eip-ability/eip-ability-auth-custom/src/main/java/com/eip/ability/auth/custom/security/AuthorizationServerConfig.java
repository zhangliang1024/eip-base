package com.eip.ability.auth.custom.security;

import cn.hutool.core.collection.CollectionUtil;
import com.eip.ability.auth.custom.client.ClientDetailsServiceImpl;
import com.eip.ability.auth.custom.system.SysUserDetails;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * ClassName: AuthorizationServerConfig
 * Function:
 * 授权相关配置
 * 1.设置oauth客户端获取信息来源 clientDetailsService
 * 2.设置默认的token存储方式
 * 3.添加token增强器（在token中添加用户信息）
 * 4.添加token加密方式
 * Date: 2022年09月05 10:09:52
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;
    @Autowired
    private SysUserAuthenticationConverter sysUserAuthenticationConverter;


    /**
     * clients客户度
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        clients.withClientDetails(clientDetailsService);
    }


    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务（token services）
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // Token增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
        endpoints
                //.authorizationCodeServices(authorizationCodeServices())  //授权码模式所需要的authorizationCodeServices
                .authenticationManager(authenticationManager)   //密码模式需要的authenticationManager
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .tokenGranter(compositeTokenGranter)
                // reuseRefreshTokens设置为false时，每次通过refresh_token获得access_token时，也会刷新refresh_token；也就是说，会返回全新的access_token与refresh_token。
                //默认值是true，只返回新的access_token，refresh_token不变。
                .reuseRefreshTokens(true)
                .tokenServices(tokenServices(endpoints))    //令牌管理服务，无论哪种模式都需要
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)  //只允许POST提交访问令牌 uri: /oauth/token
        /*
         * 默认获取token的路径是/oauth/token，通过pathMapping方法，可改变默认路径
         * pathMapping用来配置端点URL链接，有两个参数，都将以 "/" 字符为开始的字符串
         * defaultPath：这个端点URL的默认链接
         * customPath：你要进行替代的URL链接
         */
        //.pathMapping("/oauth/token", "/oauth/token")
        ;
    }

    public DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());  //令牌服务
        tokenServices.setSupportRefreshToken(true);  //支持令牌的刷新
        tokenServices.setClientDetailsService(clientDetailsService);    //客户端 配置策略
        tokenServices.setTokenEnhancer(tokenEnhancerChain);   //设置令牌增强，使用JwtAccessTokenConverter进行转换
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 2); //access_token过期时间
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 2); //refresh_token过期时间

        return tokenServices;
    }


    /**
     * JWT内容增强
     * 增加用户ID和用户名
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            HashMap<String, Object> additionalInfo = CollectionUtil.newHashMap();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof SysUserDetails) {
                SysUserDetails userDetails = (SysUserDetails) principal;
                additionalInfo.put("userId", userDetails.getUserId());
                additionalInfo.put("username", userDetails.getUsername());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            }
            return accessToken;
        };
    }


    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 设置用户转化器
        ((DefaultAccessTokenConverter) converter.getAccessTokenConverter()).setUserTokenConverter(sysUserAuthenticationConverter);
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * 秘钥库中获取秘钥对（公钥+私钥）
     * 生成秘钥
     * keytool -genkey -alias jwt -keyalg RSA -keysize 1024 -keystore jwt.jks -validity 365
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        KeyPair keyPair = factory.getKeyPair("jwt", "123456".toCharArray());
        return keyPair;
    }


}
