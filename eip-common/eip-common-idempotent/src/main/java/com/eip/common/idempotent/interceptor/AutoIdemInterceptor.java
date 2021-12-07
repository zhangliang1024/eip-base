package com.eip.common.idempotent.interceptor;

import com.eip.common.idempotent.annotation.AutoIdempotent;
import com.eip.common.idempotent.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * ClassName: AutoIdemInterceptor
 * Function: 幂等拦截处理
 * Date: 2021年12月07 16:34:35
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class AutoIdemInterceptor implements HandlerInterceptor {

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        log.info("[eip-idem] - requet-uri : {}", request.getRequestURI());
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AutoIdempotent autoIdempotent = handlerMethod.getMethodAnnotation(AutoIdempotent.class);
        if (Objects.nonNull(autoIdempotent)) {
            //幂等校验,校验通过则放行。校验失败抛出异常，并通过统一异常处理
            return tokenService.checkToken(request);
        }
        return true;
    }

    private void writeJsonResult(HttpServletResponse response, String result) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(writer)) {
                writer.close();
            }
        }
    }
}
