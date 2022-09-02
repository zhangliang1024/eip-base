package com.eip.common.gateway.custom.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ResourceServerManager
 * Function: 做对资源权限的判断
 * Date: 2022年09月02 17:39:09
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security")
public class ResourceServerManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisTemplate redisTemplate;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Setter
    private List<String> ignoreUrls;


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        // Options预检直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        String method = request.getMethodValue();
        String path = request.getURI().getPath();

        // 跳过token校验，放在这里去做就是为了能够动态刷新
        if (skipValid(path)) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 如果token为空或着不合法，则进行拦截
        String resultPath = method + ":" + path; //RESTFUL 接口权限设计
        String token = request.getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(token) || !StringUtils.startsWithIgnoreCase(token, "Bearer ")) {
            return Mono.just(new AuthorizationDecision(false));
        }

        // 从Redis中获取资源权限
        Map<String, Object> urlPermRolesRules = this.redisTemplate.opsForHash().entries("all_urls_key");
        // 拥有访问权限的角色
        List<String> authorizeRoles = new ArrayList<>();
        // 是否需要鉴权，默认未设置拦截规则不需要 鉴权
        boolean requireCheck = false;

        // 获取当前资源 所需要的角色
        for (Map.Entry<String, Object> permRoles : urlPermRolesRules.entrySet()) {
            String key = permRoles.getKey();
            if (pathMatcher.match(key, resultPath)) {
                List<String> roles = Convert.toList(String.class, permRoles.getValue());
                authorizeRoles.addAll(Convert.toList(String.class, roles));
                if (requireCheck == false) {
                    requireCheck = true;
                }
            }
        }

        // 如果资源不需要权限 则直接返回授权成功
        if (!requireCheck) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 判断JWT中携带的用户角色是否有权限访问
        Mono<AuthorizationDecision> authorizationDecisionMono = mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority -> {
                    String roleCode = authority.substring("SecurityConstants.AUTHORITY_PREFIX".length());
                    boolean hasAuthorized = CollectionUtil.isNotEmpty(authorizeRoles) && authorizeRoles.contains(roleCode);
                    return hasAuthorized;
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));

        return authorizationDecisionMono;
    }

    /**
     * 判端 请求地址是否跳过校验
     */
    private boolean skipValid(String path) {
        for (String skipPath : ignoreUrls) {
            if (pathMatcher.match(skipPath, path)) {
                return true;
            }
        }
        return false;
    }
}
