package com.eip.ability.auth.oauth2.exception;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * ClassName: AuthClientCredentialsTokenEndpointFilter
 * Function: 客户端异常处理
 * 1.自定义客户端认证过滤器，根据客户端ID、秘钥进行认证
 * 2.重写这个过滤器用于自定义异常处理
 * 3.具体的认证逻辑依然使用ClientCredentialsTokenEndpointFilter，只是设置一下
 * Date: 2022年01月18 11:31:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AuthClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {


    private final AuthorizationServerSecurityConfigurer configurer;
    private AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 构造方法
     *
     * @param configurer AuthorizationServerSecurityConfigurer对昂
     * @param entryPoint 自定义的AuthenticationEntryPoint
     */
    public AuthClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer, AuthenticationEntryPoint entryPoint) {
        this.configurer = configurer;
        this.authenticationEntryPoint = entryPoint;
    }

    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint entryPoint) {
        this.authenticationEntryPoint = entryPoint;
    }

    /**
     * 需要重写这个方法，返回AuthenticationManager
     */
    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return configurer.and().getSharedObject(AuthenticationManager.class);
    }

    /**
     * 设置AuthenticationEntryPoint主要逻辑
     * TODO 定制认证失败处理器，开发中可以自己修改
     */
    @Override
    public void afterPropertiesSet() {
        setAuthenticationFailureHandler((request, response, exception) -> {
            if (exception instanceof BadCredentialsException) {
                exception = new BadCredentialsException(exception.getMessage(), new BadClientCredentialsException());
            }
            authenticationEntryPoint.commence(request, response, exception);
        });
        //成功处理器，和父类相同，为空即可。
        setAuthenticationSuccessHandler((request, response, authentication) -> {});
    }

}
