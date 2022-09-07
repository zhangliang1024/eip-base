package com.eip.sample.oauth2.jwt.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
    public TokenStore tokenStore(){
        //使用JwtTokenStore生成JWT令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 令牌增强类
     * TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //设置秘钥
        converter.setSigningKey(SING_KEY);
        return converter;
    }

}
