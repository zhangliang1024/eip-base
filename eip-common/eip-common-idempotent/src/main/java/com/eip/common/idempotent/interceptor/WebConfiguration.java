package com.eip.common.idempotent.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * ClassName: WebConfiguration
 * Function:
 * Date: 2021年12月07 16:43:09
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    private AutoIdemInterceptor idempotentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idempotentInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger**","/v3/api-docs");
                //.excludePathPatterns("doc.html","/webjars/**","/swagger**","/v3/api-docs","/favicon.ico");
    }
}
