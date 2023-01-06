package com.eip.sample.gray.zuul.utils;

import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class ForwardUtil {
    
    public static void forward(RequestContext ctx, String forwardPath) {
        HttpServletRequest request = ctx.getRequest();
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
        if (dispatcher != null) {
            if (!ctx.getResponse().isCommitted()) {
                try {
                    dispatcher.forward(request, ctx.getResponse());
                } catch (ServletException e) {
                    log.error("请求转发失败", e);
                } catch (IOException e) {
                    log.error("请求转发失败,IO异常", e);
                    ReflectionUtils.rethrowRuntimeException(e);
                }
            }
        }
    }
}
