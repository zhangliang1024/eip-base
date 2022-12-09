package com.eip.ability.gateway.filter;

import com.eip.common.core.constants.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ClassName: GlobalTraceIdFilter
 * Function: 全局链路
 * Date: 2022年11月28 16:47:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
// @Component
public class GlobalTraceIdFilter implements GlobalFilter, Ordered {

    String GLOBAL_TRACE_ID = "tid";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = TraceContext.traceId();
        log.info("---------全局链路 : {}-------------",traceId);
        //放入请求头中
        ServerHttpRequest tokenRequest = exchange.getRequest().mutate().header(GLOBAL_TRACE_ID, traceId).build();
        ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
        return chain.filter(build);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
