package com.eip.common.cores.enums;

/**
 * ClassName: LogEventTypeEnum
 * Function: 日志事件类型
 * Date: 2022年02月11 11:17:27
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum  LogEventTypeEnum {

    SYSTEM("SYSTEM"), //系统事件

    BUSINESS("BUSINESS"),//业务事件

    ;
    private final String value;

    LogEventTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
