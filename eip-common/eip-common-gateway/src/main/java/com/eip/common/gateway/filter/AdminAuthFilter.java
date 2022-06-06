package com.eip.common.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.eip.common.gateway.config.GatewaySetting;
import com.eip.common.gateway.utils.FilterUtil;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

/**
 * Function: 登录过滤
 * Date: 2022年05月16 20:00:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class AdminAuthFilter extends AbstractGatewayFilterFactory implements Ordered {

    @Autowired
    private GatewaySetting gatewaySetting;

    @Autowired
    private RedisService redisService;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String requestURI = "/api/admin" + request.getURI().getPath();
            // 白名单过滤
            if (gatewaySetting.getWhiteUrls().contains(requestURI)) {
                return chain.filter(exchange);
            }

            // token校验
            boolean isCookieToken = false;
            String token = request.getHeaders().getFirst("Authorization");
            if (StringUtils.isEmpty(token)) {
                MultiValueMap<String, HttpCookie> cookieValueMap = request.getCookies();
                log.debug("cookieValueMap：[{}]" + JSONUtil.toJsonStr(cookieValueMap));
                if (cookieValueMap.containsKey(GlobalConstant.ADMIN_LOGIN_TOKEN_COOKIE_NAME)) {
                    HttpCookie cookie = cookieValueMap.getFirst(GlobalConstant.ADMIN_LOGIN_TOKEN_COOKIE_NAME);
                    token = cookie.getValue();
                    isCookieToken = true;
                }
            }
            if (StringUtils.isEmpty(token)) {
                return FilterUtil.failResponse(exchange, ApiResult.error("401", "非法访问"));
            }
            if (!redisService.hasKey(RedisKeyConstant.adminApiAuthLoginToken + token)) {
                return FilterUtil.failResponse(exchange, ApiResult.error("403", "登录过期"));
            }
            if (isCookieToken) {
                ServerHttpRequest host = exchange.getRequest().mutate().header("Authorization", token).build();
                ServerWebExchange build = exchange.mutate().request(host).build();
                return chain.filter(build);
            }
            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
