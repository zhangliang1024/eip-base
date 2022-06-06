package com.eip.common.gateway.filter;

import com.eip.common.gateway.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Function: 全局过滤
 * Date: 2022年05月16 20:00:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String ip = IpUtil.getIp(serverHttpRequest);
        String method = serverHttpRequest.getMethod().name();
        String requestURI = serverHttpRequest.getURI().getPath();
        String token = serverHttpRequest.getHeaders().getFirst("Authorization");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                log.info(String.format("%s >>> %s >>> %s >>> %s >>> %s ms", requestURI, method, ip, token, executeTime));
            }
        }));
    }

    @Override
    public int getOrder() {
        return -2;
    }

}
