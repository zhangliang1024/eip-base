package com.eip.oauth2.jwt.exception.res.utils;

import com.eip.common.core.constants.AuthConstants;
import com.eip.oauth2.jwt.exception.res.model.LoginUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: AuthUserUtil
 * Function: OAuth2.0工具类，从请求的线程中获取个人信息
 * Date: 2022年01月18 17:27:53
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AuthUserUtil {

    /**
     * 获取当前请求登录用户的详细信息
     */
    public static LoginUser getCurrentUser(){
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        return (LoginUser) request.getAttribute(AuthConstants.LOGIN_VAL_ATTRIBUTE);
    }
}
