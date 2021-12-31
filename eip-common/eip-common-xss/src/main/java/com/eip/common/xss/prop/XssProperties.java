package com.eip.common.xss.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.util.CollectionUtils;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
@ConfigurationProperties("eip.xss.filter")
public class XssProperties {

    private boolean enabled = true;
    private int order;
    private String name = "xssFilter";
    private Map<String, String> initParameters = new LinkedHashMap<>();
    private Set<String> servletNames = new LinkedHashSet<>();
    private Set<ServletRegistrationBean<?>> servletRegistrationBeans = new LinkedHashSet<>();
    private Set<String> urlPatterns = new LinkedHashSet<>();


    public Set<String> getUrlPatterns() {
        if(CollectionUtils.isEmpty(urlPatterns)){
            urlPatterns.add("/*");
        }
        return urlPatterns;
    }
}
