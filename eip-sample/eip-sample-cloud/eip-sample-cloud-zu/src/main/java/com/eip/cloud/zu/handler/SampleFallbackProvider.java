package com.eip.cloud.zu.handler;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @Description 微服务不可以，统一反馈
 * TODO 说明：为了记录日志，让zuul自己抛异常，转发给/error处理
 * 微服务调用 这个配置优先生效。否则会进入ErrorFilter
 */
@Slf4j
@Component
public class SampleFallbackProvider implements FallbackProvider {


    @Override
    public String getRoute() {
        // 表明是为哪个微服务提供回退,如果需要所有调用都支持回退，则return "*"或return null
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        log.error("路由回退：" + route + "；" + cause.getMessage());
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                // fallback时的状态码
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                // 数字类型的状态码，本例返回的其实就是200，详见HttpStatus
                return this.getStatusCode().value();
            }

            @Override
            public String getStatusText() throws IOException {
                // 状态文本，本例返回的其实就是OK，详见HttpStatus
                JSONObject ret = new JSONObject();
                ret.put("code", 508);
                // 请求失败，服务异常!请联系管理员或稍后再试
                ret.put("msg", "服务异常或升级中，请稍后再试。");
                return ret.toString();
            }

            @Override
            public void close() {
            }

            /**
             * 封装统一响应
             * @return
             * @throws IOException
             */
            @Override
            public InputStream getBody() throws IOException {
                log.info(route + "服务异常，调用失败");
                return new ByteArrayInputStream(getStatusText().getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }
        };
    }
}