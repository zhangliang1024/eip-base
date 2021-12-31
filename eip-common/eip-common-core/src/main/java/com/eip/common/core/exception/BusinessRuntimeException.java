package com.eip.common.core.exception;


import com.eip.common.core.core.assertion.IResponseEnum;

/**
 * 业务异常
 * 业务处理时，出现异常，可以抛出该异常
 */
public class BusinessRuntimeException extends BaseRuntimeException {

    public BusinessRuntimeException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public BusinessRuntimeException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
