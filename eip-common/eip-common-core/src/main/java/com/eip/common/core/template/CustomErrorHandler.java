package com.eip.common.core.template;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * ClassName: CustomErrorHandler
 * Function: 自定义错误处理器
 * Date: 2021年12月01 15:22:01
 * <p>
 *     1.默认RestTemplate有请求机制，状态码非200就会抛异常，会中断接下来的操作
 *     2.实现ResponseErrorHandler，重写hasError返回true。
 * </p>
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class CustomErrorHandler implements ResponseErrorHandler {


    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

    }
}
