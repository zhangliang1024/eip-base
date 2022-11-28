package com.eip.ability.gateway.config;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.eip.ability.gateway.util.SecurityUtils;
import com.eip.common.core.constants.AuthConstants;
import com.eip.common.core.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * ClassName: JwtAccessManager
 * Function: 鉴权管理器
 * 经过认证管理器JwtAuthenticationManager认证成功后，需要对令牌进行鉴权
 * 如果令牌无法访问资源，则不允许通过
 * Date: 2022年01月18 13:28:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class AuthAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    // 匹配URL
    final PathMatcher matcher = new AntPathMatcher();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 校验用户权限
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {

        // 获取请求路径和方法
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        String method = request.getMethodValue();

        String authorization = request.getHeaders().getFirst("Authorization");
        String token = StringUtils.substringAfter(authorization, "Bearer ");
        Object userId = SecurityUtils.getUserId(token);

        // 为了适配restful接口，比如 GET:/api/.. POST:/api/..  *:/api/..  星号匹配所有
        String permission = method + AuthConstants.METHOD_SUFFIX + uri.getPath();
        log.info("[AUTH ACCESS] reqeuest permission : {}", permission);

        String permKey = getKey("PERMISSIONS", String.valueOf(userId));
        Object value = this.redisService.get(permKey);
        JSONArray array = JSONUtil.parseArray(value);
        List<String> perms = JSONUtil.toList(array, String.class);

        return mono.filter(Authentication::isAuthenticated)// 判断是否认证成功
                .map(auth -> {
                    return new AuthorizationDecision(checkAuthorities(auth, perms, permission));
                }).defaultIfEmpty(new AuthorizationDecision(false));

    }

    /**
     * 权限校验
     *
     * @param perms      用户权限
     * @param permission 请求权限
     */
    private boolean checkAuthorities(Authentication auth, List<String> perms, String permission) {
        if (auth instanceof OAuth2Authentication) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            for (String perm : perms) {
                String[] permStr = StringUtils.splitByWholeSeparator(perm, AuthConstants.METHOD_SUFFIX);
                String permMethod = permStr[0], permPath = permStr[1];
                String[] requestPermStr = StringUtils.splitByWholeSeparator(permission, AuthConstants.METHOD_SUFFIX);
                String requestMethod = requestPermStr[0], requestPath = requestPermStr[1];
                if (StringUtils.endsWithIgnoreCase(permMethod, requestMethod)) {
                    if (antPathMatcher.match(permPath, requestPath)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return perms.contains(permission);
    }

    private String getKey(String prefix, String key) {
        return "wemirr-platform-authority:" + prefix + AuthConstants.METHOD_SUFFIX + key;
    }
}
