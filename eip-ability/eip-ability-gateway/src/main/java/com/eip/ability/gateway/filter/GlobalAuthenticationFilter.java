package com.eip.ability.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.eip.common.core.auth.AuthUserDetail;
import com.eip.common.core.constants.AuthConstants;
import com.eip.common.core.core.assertion.enums.AuthResponseEnum;
import com.eip.common.core.core.protocol.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * ClassName: GlobalAuthenticationFilter
 * Function: 全局过滤器，封装数据传递给下游微服务
 * ① 白名单直接放行
 * ② 校验token
 * ③ 读取token中用户信息
 * ④ 封装用户信息，放入请求头传递到下游
 * Date: 2022年01月18 13:26:59
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class GlobalAuthenticationFilter implements GlobalFilter, Ordered {


    private final AntPathMatcher matcher = new AntPathMatcher();

    @Autowired
    private TokenStore tokenStore;


    @Autowired
    private GatewayProperies properies;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getPath().value();
        //校验是否白名单 如登录、授权等放行
        if (checkUrls(requestUrl)) {
            return chain.filter(exchange);
        }

        //校验token
        String token = getToken(exchange);
        if (StringUtils.isBlank(token)) {
            return invalidTokenMono(exchange);
        }


        //3 判断是否是有效的token
        OAuth2AccessToken oAuth2AccessToken;
        try {
            //解析token，使用tokenStore
            oAuth2AccessToken = tokenStore.readAccessToken(token);
            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            //令牌的唯一ID
            String jti = additionalInformation.get(AuthConstants.JTI).toString();
            /**查看黑名单中是否存在这个jti，如果存在则这个令牌不能用****/
            Boolean hasKey = redisTemplate.hasKey(AuthConstants.JTI_KEY_PREFIX + jti);
            if (hasKey)
                return invalidTokenMono(exchange);
            //取出用户身份信息
            String userName = additionalInformation.get("user_name").toString();
            //获取用户权限
            List<String> authorities = (List<String>) additionalInformation.get("authorities");
            //从additionalInformation取出userId
            String userId = additionalInformation.get(AuthConstants.USER_ID).toString();
            String tenantId = additionalInformation.get(AuthConstants.TENANT_ID).toString();

            AuthUserDetail loginUser = new AuthUserDetail();
            loginUser.setUserId(userId);
            loginUser.setUsername(userName);
            loginUser.setTenantId(tenantId);
            //将解析后的token加密放入请求头中，方便下游微服务解析获取用户信息
            String base64 = Base64.encode(JSONUtil.toJsonStr(loginUser));
            //放入请求头中
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate().header(AuthConstants.TOKEN_NAME, base64).build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
            return chain.filter(build);
        } catch (InvalidTokenException e) {
            //解析token异常，直接返回token无效
            return invalidTokenMono(exchange);
        }
    }

    private String getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(tokenStr)) {
            return null;
        }
        String token = tokenStr.split(" ")[1];
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return token;
    }

    /**
     * 无效的token
     */
    private Mono<Void> invalidTokenMono(ServerWebExchange exchange) {
        return buildReturnMono(ApiResult.builder()
                .code(AuthResponseEnum.INVALID_TOKEN.getCode())
                .message(AuthResponseEnum.INVALID_TOKEN.getMessage())
                .build(), exchange);
    }

    private Mono<Void> buildReturnMono(ApiResult result, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset:utf-8");
        return response.writeWith(Mono.just(buffer));
    }

    private boolean checkUrls(String requestUrl) {
        List<String> urls = properies.getIgnoreUrls();

        for (String url : urls) {
            if (matcher.match(url, requestUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
