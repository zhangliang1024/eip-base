package com.eip.ability.gateway.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ClassName: GatewayProperies
 * Function: 系统参数配置 白名单
 * Date: 2022年01月18 13:32:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "eip.gateway.sys")
public class GatewayProperies {

    /**
     * 白名单
     */
    private List<String> ignoreUrls;

}
