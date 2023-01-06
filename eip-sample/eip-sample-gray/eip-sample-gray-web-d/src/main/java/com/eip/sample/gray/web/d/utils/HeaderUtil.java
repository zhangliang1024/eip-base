package com.eip.sample.gray.web.d.utils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class HeaderUtil {

    public static Map<String, String> getHeaders(HttpServletRequest request) {

        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enums = request.getHeaderNames();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
    public static String getHeader(HttpServletRequest request, String headerName) {

        Enumeration<String> enums = request.getHeaderNames();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement();
            if (StringUtils.equals(headerName, key)){
                return request.getHeader(key);
            }
        }
        return null;
    }
    public static String getDecodeHeader(HttpServletRequest request, String headerName){
        return headerValueDecode(request.getHeader(headerName));
    }
    public static String headerValueEncode(Object obj){
        if (obj == null){
            return null;
        }
        String encodeStr = JSONUtil.toJsonStr(obj);
        if (StringUtils.isBlank(encodeStr)){
            return null;
        }
        try {
            return URLEncoder.encode(encodeStr, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("字符串========{}编码失败",encodeStr);
        }
        return null;
    }

    public static String headerValueDecode(Object obj){
        if (obj == null){
            return null;
        }
        String decodeStr = obj.toString();
        if (StringUtils.isBlank(decodeStr)){
            return null;
        }
        try {
            return URLDecoder.decode(decodeStr, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("字符串========{}解码失败",decodeStr);
        }
        return null;
    }
}
