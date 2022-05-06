package com.eip.common.gateway.filter;

import com.eip.common.core.constants.GlobalConstans;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ClassName: HeaderTokenGatewayFilter
 * Function: 校验auth token
 * Date: 2022年02月16 14:21:38
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class HeaderTokenGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 从HTTP Header中寻找Auth-Token
        String name = exchange.getRequest().getHeaders().getFirst(GlobalConstans.AUTH_TOKEN);
        if(StringUtils.isNotBlank(name)){
            return chain.filter(exchange);
        }

        // 标记此次请求没有权限, 并结束这次请求
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
