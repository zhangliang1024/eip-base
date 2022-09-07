package com.eip.sample.oauth2.gateway.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * ClassName: JwtAuthenticationManager
 * Function: 认证管理器
 *           JWT认证管理器，主要作用就是对携带过来的token进行校验，如：过期时间、加密方式等
 * Date: 2022年01月18 13:29:06
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    /**
     * 使用JWT令牌进行解析
     */
    @Autowired
    private TokenStore tokenStore;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(e -> e instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((accessToken -> {
                    //解析令牌
                    OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
                    //根据access_token从数据库中获取不到OAuth2AccessToken
                    if(Objects.isNull(oAuth2AccessToken)){
                        return Mono.error(new InvalidTokenException("invalid token"));
                    }else if(oAuth2AccessToken.isExpired()){
                        return Mono.error(new InvalidTokenException("expired token"));
                    }
                    OAuth2Authentication auth2Authentication = this.tokenStore.readAuthentication(oAuth2AccessToken);
                    if(Objects.isNull(auth2Authentication)){
                        return Mono.error(new InvalidTokenException("invalid token"));
                    }else {
                        return Mono.just(auth2Authentication);
                    }
                }
                )).cast(Authentication.class);
    }
}
