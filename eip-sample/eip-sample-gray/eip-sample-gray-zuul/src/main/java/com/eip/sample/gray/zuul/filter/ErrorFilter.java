package com.eip.sample.gray.zuul.filter;

import com.eip.sample.gray.zuul.utils.ForwardUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ErrorFilter extends ZuulFilter {

    @Value("${error.path}")
    private String errorPath;

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        ctx.getRequest().setAttribute("sys.error.message", throwable.getMessage());
        log.error("过滤器中出错:", throwable);
        ForwardUtil.forward(ctx, "sys_error");
        return null;
    }
}