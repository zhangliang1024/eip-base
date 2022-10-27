package com.eip.common.web.advice;

import cn.hutool.json.JSONUtil;
import com.eip.common.core.constants.GlobalConstans;
import com.eip.common.core.core.annotation.NotResponseBody;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.web.utils.HttpServletContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * springboot使用ResponseBodyAdvice导致swagger无法显示的问题
 *  ：https://blog.csdn.net/lisuo1234/article/details/122033317
 */
@RestControllerAdvice(annotations = RestController.class)
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private WebEndpointProperties endpointProperties;
    @Value("${server.servlet.context-path}")
    private String serverContextPath;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        HttpServletRequest request = HttpServletContext.getRequest();
        String requestURI = request.getRequestURI();
        String basePath = endpointProperties.getBasePath(); // 端点/actuator
        if (StringUtils.isEmpty(basePath)) {
            basePath = serverContextPath + GlobalConstans.BASE_ACTUATOR_URL;
        }else {
            basePath = serverContextPath + basePath;
        }
        // 接口/actuator/*不进行包装
        if (requestURI.startsWith(basePath)) {
            return false;
        }
        // 如果接口返回的类型本身就是ResultVO那就没有必要进行额外的操作，返回false；如果方法上加了我们的自定义注解也没有必要进行额外的操作
        return !(returnType.getParameterType().equals(ApiResult.class) ||
                returnType.hasMethodAnnotation(NotResponseBody.class) ||
                returnType.getDeclaringClass().getName().contains("springfox"));
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // String类型不能直接包装，所以要进行些特别的处理

        if (data instanceof Void) {
            return ApiResult.ok();
        }
        if (returnType.getGenericParameterType().equals(String.class)) {
            String result = JSONUtil.toJsonStr(ApiResult.ok(data));
            return result;
        }
        // 将原本的数据包装在ResultVO里
        return ApiResult.ok(data);
    }
}
