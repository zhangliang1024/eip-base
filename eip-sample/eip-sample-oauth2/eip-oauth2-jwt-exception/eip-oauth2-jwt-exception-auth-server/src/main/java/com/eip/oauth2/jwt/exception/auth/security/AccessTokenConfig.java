package com.eip.oauth2.jwt.exception.auth.security;

import com.eip.common.core.constants.AuthConstants;
import com.eip.oauth2.jwt.exception.auth.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.LinkedHashMap;

/**
 * ClassName: AccessTokenConfig
 * Function: 令牌相关配置
 * Date: 2022年01月18 09:55:22
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class AccessTokenConfig {

    @Autowired
    private SysUserAuthenticationConverter sysUserAuthenticationConverter;
    /**
     * JWT秘钥，实际项目中需要统一配置到文件中，资源服务也需要用到
     * JWT签名使用的是对称加密，资源服务器需使用相同的秘钥进行校验和解析
     * 实际工作中 使用非对称加密方式更安全
     */
    private static final String SING_KEY = "myjszl";

    /**
     * 令牌存储策略
     * 还有两种常用的方式：
     * 1. RedisTokenStore 将令牌存储到Redis中，此种方式性能更好
     * 2. JdbcTokenStore  将令牌存储到Mysql中，需要建立表结构
     */
    @Bean
    public TokenStore tokenStore() {
        //使用JwtTokenStore生成JWT令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 令牌增强类
     * TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenEnhancer();
        // 设置用户转化器，解决refresh token丢失增强数据
        ((DefaultAccessTokenConverter) converter.getAccessTokenConverter()).setUserTokenConverter(sysUserAuthenticationConverter);
        //设置秘钥
        converter.setSigningKey(SING_KEY);
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
            //获取userDetailService中查询到用户信息
            SecurityUser user = (SecurityUser) authentication.getUserAuthentication().getPrincipal();
            //将额外的信息放入到LinkedHashMap中
            LinkedHashMap<String, Object> extendInformation = new LinkedHashMap<>();
            //设置用户的userId
            extendInformation.put(AuthConstants.USER_ID, user.getUserId());
            //添加到additionalInformation
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extendInformation);
            return super.enhance(accessToken, authentication);
        }
    }

}
