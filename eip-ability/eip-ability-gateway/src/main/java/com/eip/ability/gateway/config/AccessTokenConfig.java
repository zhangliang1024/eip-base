package com.eip.ability.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * ClassName: AccessTokenConfig
 * Function: JWT令牌配置策略
 * Date: 2022年01月18 13:28:11
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class AccessTokenConfig {

    @Value("${security.oauth2.jwt.privateKey}")
    private String privateKey;
    @Value("${security.oauth2.jwt.publicKey}")
    private String publicKey;

    /**
     * 令牌的存储策略
     */
    @Bean
    public TokenStore tokenStore() {
        //使用JWT生成令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //设置秘钥
        converter.setSigningKey(privateKey);
        converter.setVerifierKey(publicKey);
        return converter;
    }

    /**
     * JWT令牌增强，继承JwtAccessTokenConverter
     * 将业务所需额外信息放入令牌中，这样下游微服务就能拆解令牌获取
     */
    //public static class JwtAccessTokenEnhancer extends JwtAccessTokenConverter {
    //    /**重写enhance方法，在其中扩展*/
    //    @Override
    //    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    //        //获取userDetailService中查询到用户信息
    //        SecurityUser user = (SecurityUser) authentication.getUserAuthentication().getPrincipal();
    //        //将额外的信息放入到LinkedHashMap中
    //        LinkedHashMap<String, Object> extendInformation = new LinkedHashMap<>();
    //        //设置用户的userId
    //        extendInformation.put(AuthConstants.USER_ID, user.getUserId());
    //        //添加到additionalInformation
    //        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extendInformation);
    //        return super.enhance(accessToken, authentication);
    //    }
    //}

}
