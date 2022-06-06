package com.eip.common.gateway.utils;

import cn.hutool.json.JSONUtil;
import com.eip.common.core.core.protocol.response.ApiResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class FilterUtil {

    public static Mono<Void> failResponse(ServerWebExchange exchange, ApiResult result) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

}
