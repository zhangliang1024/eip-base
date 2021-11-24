package com.eip.base.common.core.core.assertion.enums;


import com.eip.base.common.core.core.assertion.CommonExceptionAssert;
import com.eip.base.common.core.core.protocol.response.BaseResult;
import com.eip.base.common.core.exception.BaseRuntimeException;

/**
 * 普通枚举定义类
 */
public enum CommonResponseEnum implements CommonExceptionAssert {

    /**
     * 成功
     */
    SUCCESS(200, "SUCCESS"),
    /**
     * 服务器繁忙
     */
    SERVER_BUSY(9998, "服务器繁忙"),
    /**
     * 服务器异常
     */
    SERVER_ERROR(9999, "服务器异常"),

    // Time
    DATE_NOT_NULL(5001, "日期不能为空"),
    DATETIME_NOT_NULL(5001, "时间不能为空"),
    TIME_NOT_NULL(5001, "时间不能为空"),
    DATE_PATTERN_MISMATCH(5002, "日期[%s]与格式[%s]不匹配，无法解析"),
    PATTERN_NOT_NULL(5003, "日期格式不能为空"),
    PATTERN_INVALID(5003, "日期格式[%s]无法识别"),


    ;

    private int code;

    private String message;

    CommonResponseEnum(int code,String message){
        this.code = code;
        this.message = message;
    }

    public static void assertSuccess(BaseResult response) {
        SERVER_ERROR.assertNotNull(response);
        int code = response.getCode();
        if(CommonResponseEnum.SUCCESS.getCode() != code){
            String msg = response.getMessage();
            throw new BaseRuntimeException(code, msg);
        }
    }
    @Override
    public int getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
