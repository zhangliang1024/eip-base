package com.eip.common.alert.config.prop;

import com.eip.common.alert.enums.AlertTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: AlertProperties
 * Function: 告警配置
 * Date: 2021年12月15 16:36:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@ConfigurationProperties(prefix = "eip.alert")
@Data
public class AlertProperties {

    /**
     * 是否开启高进
     */
    private boolean enabled;
    /**
     * 启用告警配置
     */
    private AlertTypeEnum type;
    /**
     * 接受者
     */
    private String receives;
    /**
     * 企业微信配置
     */
    private WxChatProperties wxChat;
    /**
     * 钉钉配置
     */
    private DingProperties ding;
    /**
     * 飞书配置
     */
    private LarkProperties lark;

}
