package com.eip.common.log.core.enums;

/**
 * ClassName: LogOperateResultEnum
 * Function:
 * Date: 2022年02月11 11:19:14
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum LogOperateResultEnum {

    SUCCESS("成功"),

    FAIL("失败");

    private final String value;

    LogOperateResultEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
