package com.eip.common.apidoc.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ClassName: SwaggerProperties
 * Function:
 * Date: 2022年03月14 17:38:49
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "com.eip.swagger.gateway")
public class SwaggerProperties {

    /**
     * 忽略的服务
     */
    private List<String> ingoreServerList;

    /**
     * 需要做别名映射的服务
     */
    private List<ServerAlias> serverAliasList;

    @Data
    public static class ServerAlias{

        private String serverName;

        private String serverAlias;
    }

}
