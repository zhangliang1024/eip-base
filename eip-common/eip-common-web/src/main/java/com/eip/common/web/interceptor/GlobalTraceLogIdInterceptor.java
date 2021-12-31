package com.eip.common.web.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: GlobalTraceLogIdInterceptor
 * Function:
 * Date: 2021年12月23 13:14:37
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class GlobalTraceLogIdInterceptor implements HandlerInterceptor {

    public static ThreadLocal<String> GLOBAL_TRACE = new ThreadLocal();

    public static final String TRACE_ID = "tid";
    public static final String GLOBAL_TRACE_ID = "global_trace_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(GLOBAL_TRACE_ID);
        MDC.put(TRACE_ID,traceId);
        GLOBAL_TRACE.set(traceId);
        return true;
    }
}
