package com.eip.ability.gateway.handler;

import cn.hutool.json.JSONUtil;
import com.eip.common.core.core.assertion.enums.AuthResponseEnum;
import com.eip.common.core.core.protocol.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * ClassName: GlobalErrorExceptionHandler
 * Function: 网关的全局异常处理
 *           @Order(-1)：优先级一定要比ResponseStatusExceptionHandler低
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 * Date: 2022年01月18 13:30:15
 */
@Slf4j
@Order(-2)
@Component
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        ApiResult result = ApiResult.fail(AuthResponseEnum.UNAUTHORIZED.getMessage());
        // JOSN格式返回
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        //处理TOKEN失效的异常
        if (ex instanceof InvalidTokenException) {
            result = ApiResult.fail(AuthResponseEnum.INVALID_TOKEN.getMessage());
        }

        ApiResult finalResult = result;
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                //todo 返回响应结果，根据业务需求，自己定制
                return bufferFactory.wrap(JSONUtil.toJsonStr(finalResult).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                log.error("Error writing response", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
