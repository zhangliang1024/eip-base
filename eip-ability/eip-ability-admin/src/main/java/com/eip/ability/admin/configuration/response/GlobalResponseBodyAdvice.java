package com.eip.ability.admin.configuration.response;

import com.eip.common.core.core.protocol.response.ApiResult;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * @author Levin
 **/
@Configuration
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {


    private final static String REWRITE = "1";
    private final static String RESPONSE_DATA_REWRITE = "rewrite";
    private final static List<String> IGNORE_URLS = Lists.newArrayList("/users/username","/v3/api-docs","/v3/api-docs/swagger-config");

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter methodParameter, @NonNull MediaType mediaType, @NonNull Class<? extends HttpMessageConverter<?>> aClass,
                                  @NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse) {
        final HttpHeaders requestHeaders = serverHttpRequest.getHeaders();
        String requestPath = serverHttpRequest.getURI().getPath();
        //判单当前请求是否需要经过Response统一结果封装
        String isReWrite = requestHeaders.containsKey(RESPONSE_DATA_REWRITE) ? requestHeaders.getFirst(RESPONSE_DATA_REWRITE) : REWRITE;
        serverHttpResponse.getHeaders().add(RESPONSE_DATA_REWRITE, REWRITE);
        if (IGNORE_URLS.contains(requestPath)) {
            return body;
        }
        if (REWRITE.equals(isReWrite) || StringUtils.isBlank(isReWrite)) {
            if (body == null) {
                return ApiResult.success();
            }
            if (body instanceof ApiResult) {
                return body;
            }
            if (body instanceof Byte[]) {
                return body;
            }
            return ApiResult.success(body);
        } else {
            return body;
        }
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }
}