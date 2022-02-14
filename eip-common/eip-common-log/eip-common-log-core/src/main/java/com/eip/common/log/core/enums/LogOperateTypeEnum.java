package com.eip.common.log.core.enums;

/**
 * ClassName: LogOperateTypeEnum
 * Function:
 * Date: 2022年02月11 11:17:53
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum LogOperateTypeEnum {

    ADD("ADD"),

    QUERY("QUERY"),

    UPDATE("UPDATE"),

    DELETE("DELETE"),

    EXPORT("EXPORT"),

    IMPORT("IMPORT"),

    AUTHENTICATION("AUTHENTICATION"),

    LOGIN("LOGIN"),

    LOGOUT("LOGOUT"),

    AUDIT_SUCCESS("AUDIT_SUCCESS"),//审核通过

    AUDIT_REJECT("AUDIT_REJECT"), //审核驳回

    OPERATION("OPERATION"),

    ;

    private final String value;

    LogOperateTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
