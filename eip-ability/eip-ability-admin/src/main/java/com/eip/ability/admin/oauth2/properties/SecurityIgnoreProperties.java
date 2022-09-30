package com.eip.ability.admin.oauth2.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源服务器对外直接暴露URL
 *
 * @author Levin
 * @since 2019-04-04
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "security.oauth2.client")
public class SecurityIgnoreProperties {


    private String clientId;
    private String clientSecret;
    private String accessTokenUri;

    private IgnoreUrls ignore;


    @Data
    public class IgnoreUrls {

        private List<String> webUrls = new ArrayList<>();
        private List<String> resourceUrls = new ArrayList<>();
    }

}
