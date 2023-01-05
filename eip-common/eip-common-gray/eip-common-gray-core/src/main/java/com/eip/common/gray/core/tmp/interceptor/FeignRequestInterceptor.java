// package com.eip.common.gray.core.tmp.interceptor;
//
// import com.eip.common.gray.RequestContextUtils;
// import com.eip.common.gray.config.GrayRule;
// import com.eip.common.gray.core.support.RibbonFilterContextHolder;
// import feign.RequestInterceptor;
// import feign.RequestTemplate;
// import lombok.extern.slf4j.Slf4j;
//
// import javax.servlet.http.HttpServletRequest;
// import java.util.Enumeration;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Objects;
//
// /**
//  * ClassName: FeignRequestInterceptor
//  * Function: 服务间调用，将上游
//  * Date: 2023年01月03 16:24:36
//  *
//  * @author 张良 E-mail:zhangliang01@jingyougroup.com
//  * @version V1.0.0
//  */
// @Slf4j
// public class FeignRequestInterceptor implements RequestInterceptor {
//
//     @Override
//     public void apply(RequestTemplate requestTemplate) {
//         HttpServletRequest request = RequestContextUtils.getRequest();
//         Map<String, String> headers = getHeaders(request);
//         for (Map.Entry<String, String> entry : headers.entrySet()) {
//             requestTemplate.header(entry.getKey(), entry.getValue());
//         }
//     }
//
//     /**
//      * 获取源请求头
//      */
//     private Map<String, String> getHeaders(HttpServletRequest request) {
//         Map<String, String> headers = new HashMap<>();
//         Enumeration<String> headerNames = request.getHeaderNames();
//         if (Objects.nonNull(headerNames)) {
//             while (headerNames.hasMoreElements()) {
//                 String key = headerNames.nextElement();
//                 String value = request.getHeader(key);
//                 headers.put(key, value);
//                 // 将灰度标记的请求头 透传给下游服务
//                 if (GrayRule.KEY_HTTP_HEADER_VERSION.equalsIgnoreCase(key)) {
//                     // 保存灰度标记
//                     RibbonFilterContextHolder.getCurrentContext().add(GrayRule.KEY_VERSION, value);
//                 }
//             }
//         }
//         return headers;
//     }
// }
