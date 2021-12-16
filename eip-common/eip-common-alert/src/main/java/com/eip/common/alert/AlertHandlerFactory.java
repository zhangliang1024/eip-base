package com.eip.common.alert;

import com.eip.common.alert.config.prop.AlertProperties;
import com.eip.common.alert.enums.AlertTypeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: AlertHandlerFactory
 * Function:
 * Date: 2021年12月15 17:58:56
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class AlertHandlerFactory {

    public static final Map<AlertTypeEnum, SendAlertHandler> HANDLER_MAP = new HashMap<>();

    private final AlertProperties alertProperties;

    public AlertHandlerFactory(AlertProperties alertProperties){
        this.alertProperties = alertProperties;
    }

    public SendAlertHandler getSendHandler(String type){
        AlertTypeEnum typeEnum = AlertTypeEnum.getNotifyType(type);
        SendAlertHandler sendAlertHandler = null;
        switch (typeEnum) {
            case DING:
                sendAlertHandler = HANDLER_MAP.get(typeEnum);
                break;
            case LARK:
                sendAlertHandler = HANDLER_MAP.get(typeEnum);
                break;
            case WXCHAT:
                sendAlertHandler = HANDLER_MAP.get(typeEnum);
                break;
        }
        return sendAlertHandler;
    }

}
