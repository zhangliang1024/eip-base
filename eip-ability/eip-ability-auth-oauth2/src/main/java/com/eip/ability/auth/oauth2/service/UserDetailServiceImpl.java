package com.eip.ability.auth.oauth2.service;

import com.eip.ability.auth.oauth2.feign.AdminFeignClient;
import com.eip.common.auth.core.domain.UserInfoDetails;
import com.eip.common.core.constants.AuthConstants;
import com.eip.common.core.core.protocol.response.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: UserDetailServiceImpl
 * Function:
 * Date: 2022年09月19 19:07:42
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AdminFeignClient adminFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tenantCode = request.getHeader(AuthConstants.OAUTH2_HEARDE_TENANT_CODE);
        ApiResult<UserInfoDetails> result = adminFeignClient.loadUserByUsername(username, tenantCode);
        return result.getData();
    }

}
