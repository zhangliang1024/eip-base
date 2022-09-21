package com.eip.ability.admin.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.eip.ability.admin.domain.entity.log.LoginLog;
import com.eip.ability.admin.mapper.LoginLogMapper;
import com.eip.ability.admin.service.LoginLogService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
//import com.eip.ability.admin.util.RegionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 业务实现类
 * 系统日志
 * </p>
 *
 * @author Levin
 * @since 2019-10-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl extends SuperServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    private final HttpServletRequest request;

    private static final String USER_AGENT = "User-Agent";


    @Override
    public LoginLog saveLoginLog(Long userId, String principal, String realName) {
        String ip = ServletUtil.getClientIP(request);
        final String clientId = request.getParameter("client_id");
        String region = "127.0.0.1";//RegionUtils.getRegion(ip);
        String ua = request.getHeader(USER_AGENT);
        final UserAgent userAgent = UserAgentUtil.parse(ua);
        final Browser browser = userAgent.getBrowser();
        final OS os = userAgent.getOs();
        LoginLog loginLog = LoginLog.builder()
                .userId(userId)
                .principal(principal).location(region).ip(ip)
                .platform(userAgent.getPlatform().getName())
                .engine(userAgent.getEngine().getName())
                .engineVersion(userAgent.getEngineVersion())
                .browser(browser.getName())
                .browserVersion(browser.getVersion(ua))
                .os(os.getName())
                .clientId(clientId).name(realName)
                .build();
        super.save(loginLog);
        return loginLog;
    }
}
