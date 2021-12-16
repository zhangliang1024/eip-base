package com.eip.common.alert.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: WxChatProperties
 * Function:
 * Date: 2021年12月15 17:18:07
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(prefix = "eip.alert.ding")
public class DingProperties {

    private boolean enabled;

    private String type;

    private String secretKey;
}
