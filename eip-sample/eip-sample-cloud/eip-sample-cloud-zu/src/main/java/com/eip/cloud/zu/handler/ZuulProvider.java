package com.eip.cloud.zu.handler;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zy on 2017/11/2.
 */
@Slf4j
// @Component
public class ZuulProvider implements FallbackProvider {
    private static ResponseEntity TIME_OUT_JSON_RESLUT = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    private static byte[] TIME_OUT_ERROR_BYTES = JSONUtil.toJsonStr(TIME_OUT_JSON_RESLUT).getBytes();

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        final InputStream inputStream = new ByteArrayInputStream(TIME_OUT_ERROR_BYTES);
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {
                log.info("---------ZuulProvider----------");
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.fillInStackTrace();
                }
            }

            @Override
            public InputStream getBody() throws IOException {
                return inputStream;
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }
}
