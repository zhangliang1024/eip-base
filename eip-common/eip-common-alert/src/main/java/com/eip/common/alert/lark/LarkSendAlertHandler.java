package com.eip.common.alert.lark;

import com.eip.common.alert.SendAlertHandler;
import com.eip.common.alert.AlertRequest;
import com.eip.common.alert.enums.AlertTypeEnum;

/**
 * ClassName: LarkSendAlertHandler
 * Function: 飞书告警
 * Date: 2021年12月15 11:43:51
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class LarkSendAlertHandler implements SendAlertHandler {


    @Override
    public String getType() {
        return AlertTypeEnum.LARK.name();
    }

    @Override
    public void sendAlertMessage(AlertRequest alertRequest) {

    }
}
