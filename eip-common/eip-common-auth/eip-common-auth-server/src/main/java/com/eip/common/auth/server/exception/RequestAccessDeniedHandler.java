package com.eip.common.auth.server.exception;

import com.eip.common.auth.com.exception.AuthResponseEnum;
import com.eip.common.auth.server.utils.ResponseUtil;
import com.eip.common.core.core.protocol.response.ApiResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: RequestAccessDeniedHandler
 * Function: 当认证后的用户访问受保护的资源时，权限不够，则会进入这个处理器
 * Date: 2022年01月18 11:31:19
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class RequestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ResponseUtil.response(response, ApiResult.error(AuthResponseEnum.NO_PERMISSION));
    }


}
