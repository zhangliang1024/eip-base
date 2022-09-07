package com.eip.common.web.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: SwaggerBootstrapUiDemoApplication
 * Function: api文档访问配置
 * <p>
 *     1. 全局异常处理404拦截问题，配置：
 *        spring.resources.add-mappings=false  为静态资源设置默认处理
 *        spring.mvc.throw-exception-if-no-handler-found=true
 *        以上配置，可以让全局异常处理404
 *     2. 但会导致swagger访问不了，所以需要加入静态资源的访问配置
 * </p>
 * Date: 2021年12月01 18:16:58
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class SwaggerMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
