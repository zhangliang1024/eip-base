package com.eip.oauth2.jwt.auth.res.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * ClassName: ResourcesServerConfig
 * Function: 资源服务配置类
 *   做为资源服务的配置类必须满足以下两个条件
 *    ① 继承 ResourceServerConfigurerAdapter
 *    ② 标注@EnableResourceServer
 * Date: 2022年01月17 17:34:56
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableResourceServer
//开启注解校验权限
@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true,securedEnabled = true)
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {


    /**
     * 配置资源ID和令牌校验服务
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //配置唯一资源ID
        resources.resourceId("res1")
                //配置令牌校验服务
                .tokenServices(tokenServices());
    }

    /**
     * 配置security的安全机制
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //#oauth2.hasScope()校验客户端的权限，这个all是在客户端中的scope
        http.authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                .anyRequest().authenticated();
    }

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * JWT 令牌服务配置
     */
    @Bean
    public ResourceServerTokenServices tokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        //配置令牌存储策略，使用AccessTokenConfig配置的JwtTokenStore
        services.setTokenStore(tokenStore);
        //设置令牌增强，使用JwtAccessTokenConverter进行转换
        services.setTokenEnhancer(jwtAccessTokenConverter);
        return services;
    }


}
