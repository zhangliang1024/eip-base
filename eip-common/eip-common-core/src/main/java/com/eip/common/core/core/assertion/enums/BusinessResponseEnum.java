package com.eip.common.core.core.assertion.enums;


import com.eip.common.core.core.assertion.BusinessExceptionAssert;

/**
 * 业务异常枚举定义类
 */
public enum BusinessResponseEnum implements BusinessExceptionAssert {

    PARAMS_ID_EMPTY_ERROR("500", "参数id不能为空"),
    RESULT_DATA_ERROR("500", "未找到此id结果"),
    PARAMS_DATA_EMPTY_ERROR("500", "参数对象不能为空"),
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
