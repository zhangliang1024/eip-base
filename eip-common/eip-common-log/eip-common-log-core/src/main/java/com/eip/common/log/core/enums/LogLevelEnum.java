package com.eip.common.log.core.enums;

/**
 * ClassName: LogLevelEnum
 * Function:
 * Date: 2022年02月11 11:17:10
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum LogLevelEnum {

    LOW("LOW"),

    MEDIUM("MEDIUM"),

    HIGH("HIGH"),

    ;
    private final String value;

    LogLevelEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
