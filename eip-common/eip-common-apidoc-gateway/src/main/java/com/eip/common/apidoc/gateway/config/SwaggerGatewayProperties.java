package com.eip.common.apidoc.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.spi.DocumentationType;

import java.util.List;

/**
 * Function: 网关聚合属性
 * Date: 2022年03月14 17:38:49
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(prefix = "eip.gateway.swagger")
public class SwaggerGatewayProperties {

    /**
     * swagger.gateway.enabled
     */
    private boolean enabled = true;

    /**
     * swagger interface version default:  3.0
     */
    private String swaggerVersion = DocumentationType.OAS_30.getVersion();

    /**
     * swagger api-docs default: /v3/api-docs
     */
    private String apiDocs = "/v3/api-docs";

    /**
     * header Name， 默认： X-Forwarded-Prefix
     */
    private String headerName = "X-Forwarded-Prefix";

    /**
     * context-path
     */
    private String contextPath = "context-path";

    /**
     * 忽略的服务
     */
    private List<String> ingoreServerList;


}