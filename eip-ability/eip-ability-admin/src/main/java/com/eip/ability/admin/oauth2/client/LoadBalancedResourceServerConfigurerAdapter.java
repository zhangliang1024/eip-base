package com.eip.ability.admin.oauth2.client;

import com.eip.ability.admin.oauth2.IgnoreAuthorize;
import com.eip.ability.admin.oauth2.properties.SecurityIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 带负载均衡的请求
 *
 * @author Levin
 * @since 2019-03-30
 */
@Slf4j
@AllArgsConstructor
@Import({ResourceAuthExceptionEntryPoint.class})
//@EnableConfigurationProperties(SecurityIgnoreProperties.class)
public class LoadBalancedResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {


    private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;
    private final SecurityIgnoreProperties securityIgnoreProperties;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        //允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        http.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntry : handlerMethods.entrySet()) {
            RequestMappingInfo key = handlerMethodEntry.getKey();
            HandlerMethod value = handlerMethodEntry.getValue();
            IgnoreAuthorize annotation = value.getMethodAnnotation(IgnoreAuthorize.class);
            if (log.isDebugEnabled()) {
                log.debug("[key] - [{}] - [value] - [{}]", key, value);
            }
            if (annotation != null && !annotation.web()) {
                final PatternsRequestCondition patternsCondition = key.getPatternsCondition();
                if (Objects.nonNull(patternsCondition)) {
                    patternsCondition.getPatterns().forEach(url -> registry.antMatchers(url).permitAll());
                }
                //final PathPatternsRequestCondition pathPatternsCondition = key.getPathPatternsCondition();
                //if (Objects.nonNull(pathPatternsCondition)) {
                //    pathPatternsCondition.getPatterns().forEach(url -> registry.antMatchers(url.getPatternString()).permitAll());
                //}
            }
        }
        List<String> ignoreUrls = securityIgnoreProperties.getIgnore().getResourceUrls();
        if (!CollectionUtils.isEmpty(ignoreUrls)) {
            String[] arr = ignoreUrls.toArray(new String[0]);
            registry.antMatchers(arr).permitAll();
        }
        registry.anyRequest().authenticated()
                .and().csrf().disable();


    }
}