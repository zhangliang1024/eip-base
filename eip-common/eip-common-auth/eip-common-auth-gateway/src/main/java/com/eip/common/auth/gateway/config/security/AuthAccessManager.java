package com.eip.common.auth.gateway.config.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.eip.common.core.constants.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: JwtAccessManager
 * Function: 鉴权管理器
 *           经过认证管理器JwtAuthenticationManager认证成功后，需要对令牌进行鉴权
 *           如果令牌无法访问资源，则不允许通过
 * Date: 2022年01月18 13:28:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class AuthAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //匹配URL
        AntPathMatcher matcher = new AntPathMatcher();
        //获取请求路径和方法
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        String method = authorizationContext.getExchange().getRequest().getMethodValue();
        //为了适配restful接口，比如 GET:/api/.... POST:/api/....  *:/api/.....  星号匹配所有
        String restFulPath = method + AuthConstants.METHOD_SUFFIX + uri.getPath();

        //获取所有的uri->角色对应关系
        Map<String, List<String>> entries = redisTemplate.opsForHash().entries(AuthConstants.OAUTH_URLS);
        List<String> authorities = new ArrayList<>();
        entries.forEach((path,roles) -> {
            //路径匹配则添加到角色集合中
            if (matcher.match(path, restFulPath)) {
                authorities.addAll(roles);
            }
        });

        //认证通过且角色匹配的用户可访问当前路径
        return mono
                //判断是否认证成功
                .filter(Authentication::isAuthenticated)
                //获取认证后的全部权限
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                //如果权限包含则判断为true
                .any(authority -> {
                    //  //超级管理员直接放行
                    if(StrUtil.equals(AuthConstants.ROLE_ROOT_CODE,authority)){
                        return true;
                    }
                    //其他必须要判断角色是否存在交集
                    return CollectionUtil.isNotEmpty(authorities) && authorities.contains(authority);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
