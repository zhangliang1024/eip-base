package com.eip.common.core.core.protocol.response;

import org.apache.commons.lang3.StringUtils;

/**
 * @项目名称：eip-base
 * @包名：com.eip.base.common.core.core.protocol.response
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 16:06
 * @version：V1.0
 */
public abstract class ApiResult {

    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MSG = "success";



    abstract String getCode();

    abstract String getMessage() ;

    public static  ApiResult error(String code, String message) {
        return new BaseResult(code, message);
    }

    public static  ApiResult ok() {
        return new BaseResult();
    }

    public static  ApiResult ok(String message) {
        return new BaseResult(SUCCESS_CODE,message);
    }

    public static boolean isFailure(ApiResult result) {
        return !isSuccess(result);
    }

    public static boolean isSuccess(ApiResult result) {
        return StringUtils.equals(SUCCESS_CODE, result.getCode());
    }
}
