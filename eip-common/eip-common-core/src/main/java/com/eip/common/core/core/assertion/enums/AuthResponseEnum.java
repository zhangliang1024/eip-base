package com.eip.common.core.core.assertion.enums;

import com.eip.common.core.core.assertion.AuthExceptionAssert;

/**
 * ClassName: AuthResponseEnum
 * Function:
 * Date: 2022年09月05 14:57:31
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum AuthResponseEnum implements AuthExceptionAssert {

    USERNAME_OR_PASSWORD_ERROR("A00100", "用户名或密码错误"),
    USER_NOT_EXIST("A00101", "用户不存在"),
    CLIENT_AUTHENTICATION_FAILED("A00212", "客户端认证失败"),
    ACCESS_UNAUTHORIZED("A00213", "未授权"),
    TOKEN_INVALID_OR_EXPIRED("A00214", "token非法或失效"),
    TOKEN_ACCESS_FORBIDDEN("A00215", "token禁止访问"),

    ;


    private String code;

    private String message;

    AuthResponseEnum(String code,String message){
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
