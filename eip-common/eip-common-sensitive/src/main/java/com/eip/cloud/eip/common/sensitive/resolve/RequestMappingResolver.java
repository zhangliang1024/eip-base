package com.eip.cloud.eip.common.sensitive.resolve;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * ClassName: RequestMappingResolver
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class RequestMappingResolver implements HandlerMethodResolver {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public HandlerMethod resolve() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .map(this::getHandler)
                .map(HandlerExecutionChain::getHandler)
                .filter(HandlerMethod.class::isInstance)
                .map(HandlerMethod.class::cast)
                .orElse(null);
    }

    public final HandlerExecutionChain getHandler(HttpServletRequest request) {
        try {
            return requestMappingHandlerMapping.getHandler(request);
        } catch (Exception e) {
            log.error("Cannot get handler from current HttpServletRequest: {}", e.getMessage(), e);
            return null;
        }
    }
}
