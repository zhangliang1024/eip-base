package com.eip.base.common.core.core.assertion.enums;


import com.eip.base.common.core.core.assertion.BusinessExceptionAssert;

/**
 * 业务异常枚举定义类
 */
public enum BusinessResponseEnum implements BusinessExceptionAssert {



    ;

    private int code;

    private String message;

    BusinessResponseEnum(int code, String message){
        this.code = code;
        this.message = message;
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
