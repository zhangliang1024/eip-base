package com.eip.cloud.zu.filter;

import cn.hutool.json.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@Component
public class SamplerErrorFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        // 需要在默认的 SendErrorFilter 之前
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        // 只有在抛出异常时才会进行拦截
        return true;
        // return RequestContext.getCurrentContext().containsKey("throwable");
    }

    @Override
    public Object run() {
        try {
            RequestContext requestContext = RequestContext.getCurrentContext();
            Object e = requestContext.get("throwable");
            if (e != null && e instanceof ZuulException) {

                requestContext.remove("throwable");

                // 响应信息
                HttpServletResponse response = requestContext.getResponse();
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                int status = response.getStatus();

                // 数据库-记录操作日志
                HttpServletRequest request = requestContext.getRequest();
                log.error("接口：{}；异常信息：{}", request.getRequestURI(), ((ZuulException) e).getMessage());

                // 响应给客户端信息
                PrintWriter pw = null;
                pw = response.getWriter();
                JSONObject ret = new JSONObject();
                ret.put("code", -1);
                if (status == 504) {
                    ret.put("msg", "请求超时，请稍后再试。");
                } else {
                    ret.put("msg", "服务异常或升级中，请联系管理员。");
                }
                pw.write(ret.toString());
                pw.close();
            }
        } catch (Exception ex) {
            log.error("请求路由异常：", ex);
        }

        return null;
    }
}
