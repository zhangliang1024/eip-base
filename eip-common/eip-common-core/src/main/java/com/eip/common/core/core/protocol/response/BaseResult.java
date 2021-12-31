package com.eip.common.core.core.protocol.response;

import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.assertion.enums.CommonResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础返回结果
 */
@Data
@ApiModel("返回结果")
public class BaseResult extends ApiResult{

    /**
     * 返回码
     */
    @ApiModelProperty(value = "状态码", notes = "默认200是成功")
    protected String code;
    /**
     * 返回消息
     */
    @ApiModelProperty(value = "响应信息", notes = "来说明响应情况")
    protected String message;

    public BaseResult() {
        //默认创建成功的响应
        this(CommonResponseEnum.SUCCESS);
    }

    public BaseResult(IResponseEnum responseEnum) {
        this(responseEnum.getCode(), responseEnum.getMessage());
    }

    public BaseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
