package com.eip.common.auth.server.service;

import com.eip.common.auth.com.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * ClassName: JwtTokenUserDetailsService
 * Function: 从数据库用户中根据用户名查询用户的详细信息，包括权限
 * Date: 2022年01月18 11:32:55
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthUserService authUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库中查询
        SecurityUser securityUser = authUserService.loadUserByUsername(username);
        //用户不存在则直接抛出异常 UsernameNotFoundException，security会捕获抛出异常BadCredentialsException
        if (Objects.isNull(securityUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return securityUser;
    }
}
