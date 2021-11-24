package com.eip.base.common.core.exception;

import com.eip.base.common.core.core.assertion.IResponseEnum;

/**
 * 基础异常类，所有自定义异常类都需要继承本类
 */
public class BaseRuntimeException extends RuntimeException {

    /**
     * 异常消息参数
     */
    protected Object[] args;
    /**
     * 返回码
     */
    protected IResponseEnum responseEnum;


    public BaseRuntimeException(IResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
    }

    public BaseRuntimeException(String msg) {
        super(msg);
    }

    public BaseRuntimeException(int code, String msg) {
        super(msg);
        this.responseEnum = new IResponseEnum() {
            @Override
            public int getCode() {
                return code;
            }
            @Override
            public String getMessage() {
                return msg;
            }
        };
    }

    public BaseRuntimeException(IResponseEnum responseEnum, Object[] args, String message) {
        super(message);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BaseRuntimeException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public IResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public Object[] getArgs() {
        return args;
    }
}
