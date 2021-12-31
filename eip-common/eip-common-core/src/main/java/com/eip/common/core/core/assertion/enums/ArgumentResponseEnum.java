package com.eip.common.core.core.assertion.enums;


import com.eip.common.core.core.assertion.CommonExceptionAssert;

/**
 * 参数异常枚举定义类
 */
public enum ArgumentResponseEnum implements CommonExceptionAssert {

    VALID_ERROR("500501", "参数校验异常")


    ;

    private String code;

    private String message;

    ArgumentResponseEnum(String code,String message){
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
