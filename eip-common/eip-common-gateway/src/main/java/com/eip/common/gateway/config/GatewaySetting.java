package com.eip.common.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Function: 白名单配置
 * Date: 2022年05月16 20:00:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("setting")
@Component
public class GatewaySetting {

    private List<String> whiteUrls;

}
