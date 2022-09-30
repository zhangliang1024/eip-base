package com.eip.ability.auth.oauth2.exception;

import com.eip.ability.auth.oauth2.ResponseUtil;
import com.eip.common.core.core.assertion.enums.AuthResponseEnum;
import com.eip.common.core.core.protocol.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: AuthServerAuthenticationEntryPoint
 * Function: 用于处理客户端认证出错，包括客户端ID、密码错误
 * Date: 2022年01月18 11:30:22
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class AuthServerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 认证失败处理器会调用这个方法返回提示信息
     * TODO 实际开发中可以自己定义，此处直接返回JSON数据：客户端认证失败错误提示
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseUtil.response(response,ApiResult.error(AuthResponseEnum.CLIENT_AUTHENTICATION_FAILED));
    }


}
