package com.eip.common.log.core.exception;

/**
 * ClassName: LogOperateEnum
 * Function:
 * Date: 2022年02月14 14:31:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum LogOperateEnum implements LogOperateExceptionAssert {

    VALID_ERROR("500501", "参数校验异常"),

    LOG_LEVEL_EMPTY("", ""),
    EVENT_TYPE_EMPTY("", ""),
    BUSINESS_TYPE_EMPTY("", ""),
    OPERATE_TYPE_EMPTY("", ""),
    OPERATE_MODULE_EMPTY("", ""),

    ;
    private String code;

    private String message;

    LogOperateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
