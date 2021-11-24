package com.eip.base.common.core.core.protocol.response;

import com.eip.base.common.core.core.assertion.IResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 正常返回结果
 */
@ApiModel()
public class DataResult<T> extends BaseResult {

    @ApiModelProperty(value = "响应的具体数据")
    private T data;

    public DataResult() {
        super();
    }

    public DataResult(T data) {
        super();
        this.data = data;
    }

    public DataResult(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public DataResult(IResponseEnum responseEnum, T data) {
        super(responseEnum);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
