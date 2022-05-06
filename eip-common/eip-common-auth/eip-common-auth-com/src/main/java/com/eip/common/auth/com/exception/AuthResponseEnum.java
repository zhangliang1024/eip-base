package com.eip.common.auth.com.exception;

import lombok.Getter;

/**
 * ClassName: AuthResponseEnum
 * Function:
 * Date: 2022年01月18 15:25:35
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Getter
public enum AuthResponseEnum implements AuthExceptionAssert{

    CLIENT_AUTHENTICATION_FAILED("1001", "客户端认证失败"),

    USERNAME_OR_PASSWORD_ERROR("1002", "用户名或密码错误"),

    UNSUPPORTED_GRANT_TYPE("1003", "不支持的认证模式"),

    NO_PERMISSION("403", "无权限访问！"),

    UNAUTHORIZED("401", "系统错误"),

    INVALID_TOKEN("401", "无效的token");

    private final String code;

    private final String message;

    AuthResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
