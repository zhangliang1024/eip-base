package com.eip.ability.gateway.handler;

import cn.hutool.json.JSONUtil;
import com.eip.common.core.core.protocol.response.ApiResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * ClassName: GatewayAuthenticationEntryPoint
 * Function: 自定义认证失败处理
 * Date: 2022年09月22 17:47:34
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class GatewayAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {


    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = JSONUtil.toJsonStr(ApiResult.fail(ex.getMessage()));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        //TODO 认证失败，记录审计日志
        //if(ex instanceof AuthenticationException) {
        //    System.out.println("2. udpate log to 401");
        //}else {
        //    System.out.println("2. add log 401");
        //}
        return response.writeWith(Mono.just(buffer));
    }
}
