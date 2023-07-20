package com.eip.cloud.zu.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: PreTimeFilter
 * Function:
 * Date: 2023年07月06 14:33:16
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class PostTimeFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("---------PostTimeFilter----------");
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = ctx.getRequest().getRequestURI();
        long statrtTime = (long) ctx.get("startTime");// 请求的开始时间
        int duration = (int) (System.currentTimeMillis() - statrtTime);// 请求耗时
        log.info("requestURI: {} , duration: {}ms", requestURI, duration);
        return null;
    }
}
