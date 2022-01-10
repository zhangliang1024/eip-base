package com.eip.common.web.config;

import com.eip.common.web.interceptor.GlobalTraceLogIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: WebConfiguration
 * Function:
 * Date: 2021年12月23 13:47:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private GlobalTraceLogIdInterceptor globalTraceLogIdInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalTraceLogIdInterceptor);
    }
}
