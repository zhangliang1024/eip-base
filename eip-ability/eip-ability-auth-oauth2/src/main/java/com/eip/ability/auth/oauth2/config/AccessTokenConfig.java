package com.eip.ability.auth.oauth2.config;

import com.eip.ability.auth.oauth2.converter.CustomUserAuthenticationConverter;
import com.eip.common.auth.core.domain.UserInfoDetails;
import com.eip.common.auth.core.properties.SecurityJwtProperties;
import com.eip.common.core.constants.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.LinkedHashMap;

/**
 * ClassName: AuthConfiguration
 * Function: 令牌相关配置
 * Date: 2022年01月18 09:55:22
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class AccessTokenConfig {

    private final CustomUserAuthenticationConverter customUserAuthenticationConverter;
    private final SecurityJwtProperties jwtProperties;

    /**
     * 令牌存储策略
     * 还有两种常用的方式：
     * 1. RedisTokenStore 将令牌存储到Redis中，此种方式性能更好
     * 2. JdbcTokenStore  将令牌存储到Mysql中，需要建立表结构
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 令牌增强类
     * TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenEnhancer();
        // 设置用户转化器，解决refresh token丢失增强数据
        ((DefaultAccessTokenConverter) converter.getAccessTokenConverter()).setUserTokenConverter(customUserAuthenticationConverter);
        //设置秘钥
        converter.setSigningKey(jwtProperties.getPrivateKey());
        converter.setVerifierKey(jwtProperties.getPrivateKey());
        return converter;
    }

    /**
     * JWT令牌增强，继承JwtAccessTokenConverter
     * 将业务所需额外信息放入令牌中，这样下游微服务就能拆解令牌获取
     */
    public static class JwtAccessTokenEnhancer extends JwtAccessTokenConverter {
        /**
         * 重写enhance方法，在其中扩展
         */
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            // 客户端模式
            if (AuthConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
                return accessToken;
            }
            // 获取userDetailService中查询到用户信息
            UserInfoDetails user = (UserInfoDetails) authentication.getUserAuthentication().getPrincipal();
            LinkedHashMap<String, Object> extendInformation = new LinkedHashMap<>();
            // 设置增强字段
            extendInformation.put(AuthConstants.TENANT_ID, user.getTenantId());
            extendInformation.put(AuthConstants.USER_ID, user.getUserId());
            extendInformation.put(AuthConstants.USER_NAME, user.getUsername());
            extendInformation.put(AuthConstants.TENANT_CODE, user.getTenantCode());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extendInformation);
            return super.enhance(accessToken, authentication);
        }
    }


    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }


}
