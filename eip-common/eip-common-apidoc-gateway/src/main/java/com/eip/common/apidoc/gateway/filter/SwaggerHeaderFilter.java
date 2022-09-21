package com.eip.common.apidoc.gateway.filter;

import com.eip.common.apidoc.gateway.config.SwaggerGatewayProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * - 设置X-Forwarded-Prefix
 * 如果网关路由为upms/test/{a}，在swagger会显示为test/{a，缺少了upms这个路由节点。断点源码时发现在Swagger中会根据X-Forwarded-Prefix
 * 这个Header来获取BasePath，将它添加至接口路径与host中间，这样才能正常做接口测试，而Gateway在做转发的时候并没有这个Header添加进Request，
 * 所以发生接口调试的404错误。解决思路是在Gateway里加一个过滤器来添加这个header。
 * ————————————————
 * 版权声明：本文为CSDN博主「言尭」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/erik_tse/article/details/116652064
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {

    private final SwaggerGatewayProperties swaggerGatewayProperties;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path, swaggerGatewayProperties.getApiDocs())) {
                return chain.filter(exchange);
            }
            String basePath = path.substring(0, path.lastIndexOf(swaggerGatewayProperties.getApiDocs()));
            ServerHttpRequest newRequest = request.mutate().header(swaggerGatewayProperties.getHeaderName(), basePath).build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };
    }
}
