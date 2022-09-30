package com.eip.ability.gateway.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.eip.ability.gateway.SecurityUtils;
import com.eip.ability.gateway.domain.UserInfoDetails;
import com.eip.common.core.auth.AuthUserContext;
import com.eip.common.core.auth.AuthUserDetail;
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
import org.springframework.util.Base64Utils;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    //匹配URL
    final PathMatcher matcher = new AntPathMatcher();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {

        //获取请求路径和方法
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        String method = request.getMethodValue();

        String authorization = request.getHeaders().getFirst("Authorization");
        //if(StringUtils.isNotBlank(authorization)) {
        String token = StringUtils.substringAfter(authorization, "Bearer ");
        Object userId = SecurityUtils.getUserId(token);
        //}


        //为了适配restful接口，比如 GET:/api/.... POST:/api/....  *:/api/.....  星号匹配所有
        String restFulPath = method + AuthConstants.METHOD_SUFFIX + uri.getPath();
        log.info("path:{}", restFulPath);
        //UserInfoDetails authInfo = SecurityUtils.getAuthInfo();
        //获取所有的uri->角色对应关系
        //Map<String, List<String>> entries = redisTemplate.opsForHash().entries(AuthConstants.OAUTH_URLS);
        AuthUserDetail userDetail = AuthUserContext.get();
        //String userId = userDetail.getUserId();
        String permKey = getKey("perm", String.valueOf(userId));
        Object value = this.redisService.get(permKey);
        JSONArray array = JSONUtil.parseArray(value);
        List<String> perms = JSONUtil.toList(array, String.class);


        List<String> authorities = new ArrayList<>();
        //entries.forEach((path,roles) -> {
        //    //路径匹配则添加到角色集合中
        //    if (matcher.match(path, restFulPath)) {
        //        authorities.addAll(roles);
        //    }
        //});

        //认证通过且角色匹配的用户可访问当前路径
        //return mono
        //        .filter(Authentication::isAuthenticated) //判断是否认证成功
        //        .flatMapIterable(Authentication::getAuthorities) //获取认证后的全部权限
        //        .map(GrantedAuthority::getAuthority)
        //        //如果权限包含则判断为true/
        //        .any(authority -> {
        //            if (StrUtil.equals(AuthConstants.ROLE_ROOT_CODE, authority)) {//超级管理员直接放行
        //                return true;
        //            }
        //            return CollectionUtil.isNotEmpty(authorities) && authorities.contains(authority); //其他必须要判断角色是否存在交集
        //        })
        //        .map(AuthorizationDecision::new)
        //        .defaultIfEmpty(new AuthorizationDecision(false));

        return mono.map(auth -> {
            return new AuthorizationDecision(checkAuthorities(auth, perms,restFulPath));
        }).defaultIfEmpty(new AuthorizationDecision(false));

    }

    /**
     * 权限校验
     *
     * @param auth        用户权限
     * @param requestPath 请求路径
     * @return
     * @author http://www.javadaily.cn
     */
    private boolean checkAuthorities(Authentication auth, List<String> perms,String requestPath) {
        if (auth instanceof OAuth2Authentication) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

            return true;
            //return perms.stream()
            //        //.map(GrantedAuthority::getAuthority)
            //        .anyMatch(permission -> antPathMatcher.match(permission, requestPath));
        }

        return false;
    }

    private String getKey(String prefix, String key) {
        return prefix + ":" + key;
    }
}
