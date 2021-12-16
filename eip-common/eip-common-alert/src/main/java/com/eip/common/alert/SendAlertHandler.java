package com.eip.common.alert;

/**
 * ClassName: SendAlertHandler
 * Function: 告警接口
 * Date: 2021年12月15 10:09:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface SendAlertHandler {


    String getType();


    void sendAlertMessage(AlertRequest alertRequest);

}
