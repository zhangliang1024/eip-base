package com.eip.common.core.core.assertion.enums;


import com.eip.common.core.core.assertion.BusinessExceptionAssert;

/**
 * 业务异常枚举定义类
 */
public enum BusinessResponseEnum implements BusinessExceptionAssert {



    ;

    private String code;

    private String message;

    BusinessResponseEnum(String code, String message){
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
