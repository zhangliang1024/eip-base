package com.eip.common.core.core.exception;

import com.eip.common.core.core.assertion.IResponseEnum;

/**
 * OAuth2 校验异常，所有自定义异常类都需要继承本类
 */
public class AuthRuntimeException extends BaseRuntimeException {

    public AuthRuntimeException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public AuthRuntimeException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
