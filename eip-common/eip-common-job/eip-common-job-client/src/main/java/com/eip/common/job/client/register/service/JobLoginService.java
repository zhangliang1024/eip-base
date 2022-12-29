package com.eip.common.job.client.register.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.eip.common.job.client.config.XxlJobAutoConfig;
import com.eip.common.job.client.register.XxlJobAutoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ClassName: JobLoginService
 * Function: 在调用业务接口前，需要通过登录接口获取cookie
 * Date: 2022年12月20 14:23:49
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class JobLoginService {

    @Autowired
    private XxlJobAutoConfig properties;

    private final String TOKEN_KEY = "XXL_JOB_LOGIN_IDENTITY";
    private final Map<String, String> loginCookie = new HashMap<>();

    /**
     * 调用登录接口获取cookie并进行缓存
     */
    public void login() {
        String url = properties.getAdminAddresses().concat("/login");
        HttpResponse response = HttpRequest.post(url)
                .form("userName", properties.getUsername())
                .form("password", properties.getPassword())
                .execute();
        List<HttpCookie> cookies = response.getCookies();
        Optional<HttpCookie> cookieOpt = cookies.stream()
                .filter(cookie -> cookie.getName().equals(TOKEN_KEY))
                .findFirst();
        if (!cookieOpt.isPresent()) {
            throw new XxlJobAutoException("[eip-xxl-job] get xxl-job cookie error!");
        }
        String value = cookieOpt.get().getValue();
        loginCookie.put(TOKEN_KEY, value);
    }

    /**
     * 从缓存获取cookie，若无则调用登录接口获取。最多允许调用登录接口三次
     */
    public String getCookie() {
        for (int i = 0; i < 3; i++) {
            String cookieStr = loginCookie.get(TOKEN_KEY);
            if (StringUtils.isNotBlank(cookieStr)) {
                return TOKEN_KEY.concat("=").concat(cookieStr);
            }
            login();
        }
        throw new RuntimeException("[eip-xxl-job] get xxl-job cookie error!");
    }
}
