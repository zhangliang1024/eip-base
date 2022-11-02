package com.eip.ability.admin.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * todo 实际开发建议注释或者删除
 * 演示环境拦截器
 *
 * @author Levin
 */
@Configuration
public class CustomWebMvcConfigure implements WebMvcConfigurer {

    // 不拦截Url [/**/v3匹配设置项目路径] [/v3/** 匹配/v3开头的所有路径]
    // 路径要分开配置，放一起不生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestTokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/swagger-ui/**")
                .excludePathPatterns("/**/v3/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}