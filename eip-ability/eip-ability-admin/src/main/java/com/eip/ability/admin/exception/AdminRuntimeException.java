package com.eip.ability.admin.exception;


import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.exception.BaseRuntimeException;

/**
 * 业务异常
 * 业务处理时，出现异常，可以抛出该异常
 */
public class AdminRuntimeException extends BaseRuntimeException {

    public AdminRuntimeException(String msg) {
        super(msg);
    }


    public AdminRuntimeException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public AdminRuntimeException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
