package com.eip.common.web.interceptor;

import com.eip.common.core.constants.GlobalConstans;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: GlobalLogInterceptor
 * Function:
 * Date: 2021年12月23 13:14:37
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class GlobalLogInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(GlobalConstans.GLOBAL_TRACE_ID);
        if(StringUtils.isBlank(traceId)){
            MDC.put(GlobalConstans.TRACE_LOG_ID,traceId);
            GlobalConstans.GLOBAL_TRACE.set(traceId);
        }
        return true;
    }
}
