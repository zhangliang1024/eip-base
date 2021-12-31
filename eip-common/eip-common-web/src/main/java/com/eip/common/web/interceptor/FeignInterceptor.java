package com.eip.common.web.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * ClassName: FeignInterceptor
 * Function:
 * Date: 2021年12月23 14:00:58
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = GlobalTraceLogIdInterceptor.GLOBAL_TRACE.get();
        requestTemplate.header(GlobalTraceLogIdInterceptor.GLOBAL_TRACE_ID,traceId);
    }
}
