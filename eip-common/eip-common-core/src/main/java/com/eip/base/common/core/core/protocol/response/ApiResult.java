package com.eip.base.common.core.core.protocol.response;

/**
 * @项目名称：eip-base
 * @包名：com.eip.base.common.core.core.protocol.response
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 16:06
 * @version：V1.0
 */
public abstract class ApiResult {


    abstract int getCode();

    abstract String getMessage() ;
}
