package com.eip.common.core.core.protocol.response;

import com.eip.common.core.constants.GlobalConstans;
import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.assertion.enums.BaseResponseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 16:06
 */
@Data
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@ApiModel("返回结果")
public class ApiResult<T> implements Serializable {

    @ApiModelProperty(value = "状态码", notes = "默认200是成功")
    protected String code;

    @ApiModelProperty(value = "响应信息", notes = "来说明响应情况")
    protected String message;

    @ApiModelProperty(value = "全局logTraceId", notes = "全局递传logTraceId")
    private String logTraceId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "响应数据")
    public T data;

    private ApiResult(String code, String message) {
        this.code = code;
        this.message = message;
        this.logTraceId = MDC.get(GlobalConstans.GLOBAL_TRACE_ID);
    }

    private ApiResult() {
        //默认创建成功的响应
        this(BaseResponseEnum.SUCCESS);
    }

    private ApiResult(IResponseEnum responseEnum) {
        this(responseEnum.getCode(), responseEnum.getMessage());
    }

    private ApiResult(T data) {
        this(BaseResponseEnum.SUCCESS);
        this.data = data;
    }

    private ApiResult(String message, T data) {
        this.message = message;
        this.data = data;
    }

    private ApiResult(Throwable e) {
        this.code = BaseResponseEnum.SERVER_ERROR.getCode();
        this.message = e.getMessage();
    }

    public static ApiResult ok() {
        return new ApiResult();
    }

    public static ApiResult ok(Object data) {
        return new ApiResult(data);
    }

    public static ApiResult error() {
        return new ApiResult(BaseResponseEnum.SERVER_ERROR);
    }

    public static ApiResult error(BaseResponseEnum responseEnum) {
        return new ApiResult(responseEnum);
    }

    public static ApiResult error(String message) {
        return ApiResult.builder()
                .code(BaseResponseEnum.SERVER_ERROR.getCode())
                .message(message)
                .logTraceId(MDC.get(GlobalConstans.GLOBAL_TRACE_ID))
                .build();
    }

    public static ApiResult error(String code, String message) {
        return ApiResult.builder()
                .code(code)
                .message(message)
                .logTraceId(MDC.get(GlobalConstans.GLOBAL_TRACE_ID))
                .build();
    }

    public static boolean isFailure(ApiResult result) {
        return !isSuccess(result);
    }

    public static boolean isSuccess(ApiResult result) {
        return StringUtils.equals(BaseResponseEnum.SUCCESS.getCode(), result.getCode());
    }

}
