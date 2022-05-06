package com.eip.common.auth.client.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.eip.common.auth.com.model.LoginUser;
import com.eip.common.core.constants.AuthConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: AuthenticationFilter
 * Function: 微服务过滤器
 *           解密网关传递用户信息，将其放入request中，便于后期业务方法直接获取用户信息
 * Date: 2022年01月18 16:01:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    /**
     * 具体方法主要分为两步
     * 1. 解密网关传递的信息
     * 2. 将解密之后的信息封装放入到request中
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的加密的用户信息
        String token = request.getHeader(AuthConstants.TOKEN_NAME);
        if (StrUtil.isNotBlank(token)){
            //解密
            String json = Base64.decodeStr(token);
            //获取用户身份信息、权限信息
            LoginUser login = JSONUtil.toBean(json,LoginUser.class);
            //放入request的attribute中
            request.setAttribute(AuthConstants.LOGIN_VAL_ATTRIBUTE,login);
        }
        filterChain.doFilter(request,response);
    }
}
