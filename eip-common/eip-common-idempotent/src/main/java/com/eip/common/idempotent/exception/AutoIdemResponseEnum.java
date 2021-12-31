package com.eip.common.idempotent.exception;

import com.eip.common.core.core.assertion.BusinessExceptionAssert;

/**
 * ClassName: AutoIdemResponseEnum
 * Function: 幂等组件异常定义
 * Date: 2021年12月07 18:16:21
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum AutoIdemResponseEnum implements BusinessExceptionAssert {


    OPERATE_EXCEPTION("500500","服务端异常"),
    REQUEST_REPEAT("500501","重复请求"),
    REQUEST_TOKEN_IS_NULL("500502","请求TOKEN为空"),
    REQUEST_TOKEN_ERROR("500503","请求TOKEN错误"),

    ;


    private String code;
    private String message;

    AutoIdemResponseEnum(String code, String message){
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
