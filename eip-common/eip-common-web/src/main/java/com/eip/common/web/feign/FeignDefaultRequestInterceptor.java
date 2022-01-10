package com.eip.common.web.feign;

import com.eip.common.core.utils.HttpServletUtils;
import com.eip.common.web.interceptor.GlobalTraceLogIdInterceptor;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * ClassName: FeignInterceptor
 * Function: Feign默然拦截器
 *           将当前服务的相关参数封装到当前请求协议中，继续向下传递
 * Date: 2021年12月23 14:00:58
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class FeignDefaultRequestInterceptor implements RequestInterceptor {

    @Autowired
    private FeignProperties feignProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        List<String> sendParamNames = feignProperties.getSendParamNames();
        log.debug("[Feign-Interceptor] - sendParams : {} ",sendParamNames);

        HttpServletRequest request = HttpServletUtils.getRequest();
        //获得所有请求头数据
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            //若需要传递，则放入feign请求头中
            if(sendParamNames.contains(headerName)){
                String headerValue = request.getHeader(headerName);
                requestTemplate.header(headerName,headerValue);
            }
        }

        //获取所有请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        for(Map.Entry<String,String[]> paramEntry : parameterMap.entrySet()){
            if(sendParamNames.contains(paramEntry.getKey())){
                requestTemplate.query(paramEntry.getKey(),paramEntry.getValue());
            }
        }
    }
}
