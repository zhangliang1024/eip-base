package com.eip.common.alert.enums;

/**
 * ClassName: AlertTypeEnum
 * Function: 通知类型
 * Date: 2021年12月15 10:07:20
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum AlertTypeEnum {

    /**
     * 钉钉
     */
    DING,

    /**
     * 飞书
     */
    LARK,

    /**
     * 企业微信
     */
    WXCHAT;

    public static AlertTypeEnum getNotifyType(String name){
        return valueOf(name);
    }

}
