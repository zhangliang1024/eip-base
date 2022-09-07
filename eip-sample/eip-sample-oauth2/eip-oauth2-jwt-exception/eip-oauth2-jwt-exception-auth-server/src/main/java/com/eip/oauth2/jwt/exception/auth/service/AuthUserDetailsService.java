package com.eip.oauth2.jwt.exception.auth.service;

import cn.hutool.core.collection.CollectionUtil;
import com.eip.oauth2.jwt.exception.auth.model.SecurityUser;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: JwtTokenUserDetailsService
 * Function: 从数据库用户中根据用户名查询用户的详细信息，包括权限
 * Date: 2022年01月18 11:32:55
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service("userDetailsService")
public class AuthUserDetailsService implements UserDetailsService {

    public static List<SecurityUser> users = new ArrayList<>();

    static {
        SecurityUser admin = SecurityUser.builder()
                .userId(100L)
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("123"))
                .authorities(AuthorityUtils.createAuthorityList("ROLE_user","ROLE_admin"))
                .build();
        SecurityUser user = SecurityUser.builder()
                .userId(200L)
                .username("user")
                .password(new BCryptPasswordEncoder().encode("123"))
                .authorities(AuthorityUtils.createAuthorityList("ROLE_user"))
                .build();
        users.add(admin);
        users.add(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库中查询
        List<SecurityUser> list = users.stream().filter(e -> username.equals(e.getUsername())).limit(1)
                .collect(Collectors.toList());
        //用户不存在则直接抛出异常 UsernameNotFoundException，security会捕获抛出异常BadCredentialsException
        if (CollectionUtil.isEmpty(list)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return list.get(0);
    }
}
