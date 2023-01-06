package com.eip.sample.gray.zuul.filter;

import com.eip.common.gray.core.handler.GrayServiceHandlerChain;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Configuration
@Slf4j
public class AbFilter extends ZuulFilter {

    @Autowired
    private RouteLocator routeLocator;
    @Autowired
    private UrlPathHelper urlPathHelper;
    @Autowired
    private GrayServiceHandlerChain serviceLancherHandlerChain;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        String serviceId = getServiceId();
        if (StringUtils.isBlank(serviceId)) {
            return null;
        }
        serviceLancherHandlerChain.handle(serviceId);
        return null;
    }

    /**
     * 获取zuul 需要转发的serviceId
     *
     * @return
     */
    protected String getServiceId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (Objects.isNull(request)) {
            return null;
        }
        return getServiceId(request);
    }

    /**
     * 获取zuul需要转发的serviceId
     *
     * @param request
     * @return
     */
    protected String getServiceId(HttpServletRequest request) {
        Route route = route(request);
        if (Objects.isNull(route)) {
            return null;
        }
        String serviceId = route.getId();
        if (StringUtils.isBlank(serviceId)) {
            return null;
        }
        return serviceId;
    }

    // 核心逻辑，获取请求路径，利用RouteLocator返回路由信息
    protected Route route(HttpServletRequest request) {
        String requestURI = urlPathHelper.getPathWithinApplication(request);
        return routeLocator.getMatchingRoute(requestURI);
    }
}
