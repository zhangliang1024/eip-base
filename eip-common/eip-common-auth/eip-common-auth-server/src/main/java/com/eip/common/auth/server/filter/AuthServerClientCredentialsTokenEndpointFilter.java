package com.eip.common.auth.server.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * ClassName: AuthServerClientCredentialsTokenEndpointFilter
 * Function: 客户端异常处理
 *           自定义客户端认证过滤器，根据客户端ID、秘钥进行认证
 *           重写这个过滤器用于自定义异常处理
 *           具体的认证逻辑依然使用ClientCredentialsTokenEndpointFilter，只是设置一下
 * Date: 2022年01月18 11:31:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AuthServerClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {

    private final AuthorizationServerSecurityConfigurer configurer;

    private AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 构造方法
     *
     * @param configurer               AuthorizationServerSecurityConfigurer对昂
     * @param authenticationEntryPoint 自定义的AuthenticationEntryPoint
     */
    public AuthServerClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer,
                                                          AuthenticationEntryPoint authenticationEntryPoint) {
        this.configurer = configurer;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
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
     */
    @Override
    public void afterPropertiesSet() {
        //TODO 定制认证失败处理器，开发中可以自己修改
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
