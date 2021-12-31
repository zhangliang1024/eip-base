package com.eip.common.core.template;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * ClassName: CustomClientHttpRequestInterceptor
 * Function: 配置RestTemplate拦截器
 * Date: 2021年12月01 14:34:08
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //打印请求明细
        logRequestDetails(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        //打印响应明细
        logResponseDetails(response);
        return response;
    }

    private void logRequestDetails(HttpRequest request, byte[] body) {
        log.debug("=========================request begin======================");
        log.debug("Headers: {}", request.getHeaders());
        log.debug("body: {}", new String(body, Charsets.UTF_8));
        log.debug("method: {} , uri: {}", request.getMethod(), request.getURI());
        log.debug("=========================request end=========================");
    }

    private void logResponseDetails(ClientHttpResponse response) throws IOException {
        log.debug("=======================response begin=====================");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Status text  : {}", response.getStatusText());
        log.debug("Headers      : {}", response.getHeaders());
        log.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        log.debug("=======================response end=========================");
    }
}
