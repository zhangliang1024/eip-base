package com.eip.ability.admin.configuration;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.eip.common.core.auth.AuthUserContext;
import com.eip.common.core.auth.AuthUserDetail;
import com.eip.common.core.constants.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: TokenInterceptor
 * Function:
 * Date: 2022年09月29 19:08:36
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class RequestTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(AuthConstants.TOKEN_NAME);
        String uri = request.getRequestURI();
        log.info("reqeust url : {}", uri);
        if (StringUtils.isNotBlank(token)) {
            String base64 = Base64.decodeStr(token);
            AuthUserDetail detail = JSONUtil.toBean(base64, AuthUserDetail.class);
            AuthUserContext.set(detail);
        }
        return true;
    }
}
