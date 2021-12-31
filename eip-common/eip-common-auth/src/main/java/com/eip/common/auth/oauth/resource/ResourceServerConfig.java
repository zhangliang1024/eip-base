package com.eip.common.auth.oauth.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * ClassName: ResourceServerConfig
 * Function: 资源服务配置类
 * Date: 2021年12月17 18:50:50
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 配置资源ID和令牌校验服务
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("res")
                .tokenServices(remoteTokenServices());
    }

    /**
     * 配置security的安全机制
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                .anyRequest().authenticated();

    }

    /**
     * 注意：远程校验令牌存在性能问题，后续使用JWT令牌即可本地校验
     */
    @Bean
    public RemoteTokenServices remoteTokenServices(){
        //远程调用授权服务的check_token进行令牌校验
        RemoteTokenServices services = new RemoteTokenServices();
        //认证中心校验token的端点
        services.setCheckTokenEndpointUrl("http://loclhost:8003/ouath/check-token");
        //客户端唯一ID
        services.setClientId("client");
        //客户端秘钥
        services.setClientSecret("123");
        return services;
    }
}
