package com.eip.sample.gray.zuul.filter;

import com.eip.common.gray.core.support.RibbonFilterContextHolder;
import com.eip.sample.gray.zuul.comm.RibbonFilterMapHolder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ClearFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RibbonFilterContextHolder.clearCurrentContext();
//        todo 测试ctx是否需要clear
//        RequestContext ctx = RequestContext.getCurrentContext();
//        ctx.clear();
        RibbonFilterMapHolder.remove();
        return null;
    }
}
