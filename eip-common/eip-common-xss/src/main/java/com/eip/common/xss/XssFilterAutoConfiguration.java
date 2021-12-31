package com.eip.common.xss;

import com.eip.common.xss.filter.XssFilter;
import com.eip.common.xss.prop.XssProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

@Configuration
@EnableConfigurationProperties({XssProperties.class})
@ConditionalOnProperty(prefix = "eip.xss.filter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class XssFilterAutoConfiguration {

    @Bean("registrationXssFilter")
    public FilterRegistrationBean registrationXssFilter(XssProperties prop) {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤路径
        registrationBean.setEnabled(true);
        // 设置顺序
        registrationBean.setOrder(prop.getOrder());
        // 设置 BodyCacheFilter
        registrationBean.setFilter(new XssFilter());
        final String name = prop.getName();
        if (!StringUtils.isEmpty(name)) {
            registrationBean.setName(name);
        }
        final Map<String, String> initParameters = prop.getInitParameters();
        if (initParameters != null && initParameters.size() > 0) {
            registrationBean.setInitParameters(initParameters);
        }
        final Set<ServletRegistrationBean<?>> registrationBeans = prop.getServletRegistrationBeans();
        if (registrationBeans != null && registrationBeans.size() > 0) {
            registrationBean.setServletRegistrationBeans(registrationBeans);
        }
        final Set<String> servletNames = prop.getServletNames();
        if (!CollectionUtils.isEmpty(servletNames)) {
            registrationBean.setServletNames(servletNames);
        }
        final Set<String> urlPatterns = prop.getUrlPatterns();
        if (!CollectionUtils.isEmpty(urlPatterns)) {
            registrationBean.setUrlPatterns(urlPatterns);
        }
        return registrationBean;
    }
}
