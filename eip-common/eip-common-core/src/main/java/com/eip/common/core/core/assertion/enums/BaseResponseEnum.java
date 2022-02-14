package com.eip.common.core.core.assertion.enums;


import com.eip.common.core.core.assertion.CommonExceptionAssert;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.core.core.exception.BaseRuntimeException;

/**
 * 普通枚举定义类
 */
public enum BaseResponseEnum implements CommonExceptionAssert {

    SUCCESS("200", "success"),
    SERVER_BUSY("9998", "服务器繁忙"),
    SERVER_ERROR("500", "服务器异常"),

    // Time
    DATE_NOT_NULL("5001", "日期不能为空"),
    DATETIME_NOT_NULL("5001", "时间不能为空"),
    TIME_NOT_NULL("5001", "时间不能为空"),
    DATE_PATTERN_MISMATCH("5002", "日期[%s]与格式[%s]不匹配，无法解析"),
    PATTERN_NOT_NULL("5003", "日期格式不能为空"),
    PATTERN_INVALID("5003", "日期格式[%s]无法识别"),


    ;

    private String code;

    private String message;

    BaseResponseEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    public static void assertSuccess(ApiResult response) {
        SERVER_ERROR.assertNotNull(response);
        String code = response.getCode();
        if(!BaseResponseEnum.SUCCESS.getCode().equalsIgnoreCase(code)){
            String msg = response.getMessage();
            throw new BaseRuntimeException(code, msg);
        }
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
