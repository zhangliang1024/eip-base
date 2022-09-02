package com.eip.common.gateway.custom.filter;

import cn.hutool.core.util.StrUtil;
import com.nimbusds.jose.JWSObject;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

/**
 * ClassName: SecurityGlobalFilter
 * Function: 自定义网关过滤器，用来将解析后的token信息存放请求头中，转发给各个服务
 * Date: 2022年09月02 18:23:55
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class SecurityGlobalFilter implements GlobalFilter, Ordered {

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 不是正确的JWT不做解析处理
        String token = request.getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(token) || !StringUtils.startsWithIgnoreCase(token, "Bearer ")) {
            return chain.filter(exchange);
        }

        // 解析JWT获取jti，以jti为key判断redis的黑名单列表是否存在，存在则拦截
        token = StringUtils.replaceIgnoreCase(token,"Bearer", Strings.EMPTY);
        String payload = StrUtil.toString(JWSObject.parse(token).getPayload());
        exchange.getRequest().mutate()
                .header("token", URLEncoder.encode(payload,"UTF-8"))
                .build();
        exchange.mutate().request(request).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
