package com.eip.common.alert.constant;

/**
 * ClassName: AlertConstant
 * Function:
 * Date: 2021年12月15 10:25:59
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AlertConstant {

    public static final int SUCCES_RESULT_CODE = 0;

    public static final int REQUEST_TIME_OUT = 5 * 1000;

    public static final String SUCCESS_CODE = "200";

    public static final String SYSTEM_ERROR_CODE = "500";

    /**
     * 钉钉机器人Url
     */
    public static final String DING_ROBOT_SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=";
    /**
     * 企微机器人Url
     */
    public static final String WE_CHAT_ROBOT_SERVER_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";
    /**
     * 飞书机器人Url
     */
    public static final String LARK__ROBOT_SERVER_URL = "https://open.feishu.cn/open-apis/bot/v2/hook/";



}
