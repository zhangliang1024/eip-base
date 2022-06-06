package com.eip.common.gateway.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class IpUtil {

    private static final Log log = LogFactory.getLog(IpUtil.class);

    public static String getIp(ServerHttpRequest request) {
        String ip = null;

        List<String> headers = request.getHeaders().get("X-Real-IP");
        if (headers != null && headers.size() >= 1)
            ip = headers.get(0);

        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            log.debug(">>>>>>>>>>>>>>>>>>>>>X-Real-IP获取到ip：" + ip);
            return ip;
        }
        headers = request.getHeaders().get("X-Forwarded-For");
        if (!CollectionUtils.isEmpty(headers) && headers.size() >= 1) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            ip = headers.get(0);
            int index = ip.indexOf(',');
            if (index != -1) {
                log.debug(">>>>>>>>>>>>>>>>>>>>>X-Forwarded-For获取到ip：" + ip);
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            log.debug(">>>>>>>>>>>>>>>>>>>>>RemoteAddress获取到ip：" + ip);
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
    }
}
