package com.eip.base.common.core.core.assertion;

/**
 * 异常返回码枚举接口
 */
public interface IResponseEnum {

    /**
     * 获取返回码
     */
    int getCode();


    /**
     * 获取返回消息
     */
    String getMessage();

}
