package com.eip.common.alert.config;

import com.eip.common.alert.AlertHandlerFactory;
import com.eip.common.alert.SendAlertHandler;
import com.eip.common.alert.config.prop.AlertProperties;
import com.eip.common.alert.config.prop.DingProperties;
import com.eip.common.alert.config.prop.LarkProperties;
import com.eip.common.alert.config.prop.WxChatProperties;
import com.eip.common.alert.ding.DingSendAlertHandler;
import com.eip.common.alert.enums.AlertTypeEnum;
import com.eip.common.alert.lark.LarkSendAlertHandler;
import com.eip.common.alert.wxchat.WeChatSendAlertHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version：V1.0
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "eip.alert", name = "enabled", havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties({AlertProperties.class, WxChatProperties.class, DingProperties.class, LarkProperties.class})
public class AlertAutoConfiguration {

    public SendAlertHandler sendAlertHandler;

    @Bean
    public AlertHandlerFactory sendMessageHandlerFactory(AlertProperties alertProperties){
        AlertTypeEnum type = alertProperties.getType();
        log.info("[eip-alert] enable type - {} ",type);
        if(AlertTypeEnum.DING.equals(type) && alertProperties.getDing().isEnabled()){
            sendAlertHandler = new DingSendAlertHandler();
            AlertHandlerFactory.HANDLER_MAP.put(type, sendAlertHandler);
        }else if(AlertTypeEnum.LARK.equals(type) && alertProperties.getLark().isEnabled()){
            sendAlertHandler = new LarkSendAlertHandler();
            AlertHandlerFactory.HANDLER_MAP.put(type, sendAlertHandler);
        }else if(AlertTypeEnum.WXCHAT.equals(type) && alertProperties.getWxChat().isEnabled()){
            sendAlertHandler = new WeChatSendAlertHandler();
            AlertHandlerFactory.HANDLER_MAP.put(type, sendAlertHandler);
        }
        return new AlertHandlerFactory(alertProperties);
    }
}
