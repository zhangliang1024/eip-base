package com.eip.sample.oauth2.custom.auth.system;

import com.eip.sample.oauth2.custom.auth.enums.PasswordEncoderTypeEnum;
import com.eip.sample.oauth2.custom.auth.mobile.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ClassName: SysUserDetailsService
 * Function: 实现通过用户名获取用户信息的逻辑
 * Date: 2022年09月05 09:34:06
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service("userDetailsService")
public class SysUserDetailsService implements UserDetailsService, ISysUserService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过数据库查询用户信心
        SysUserDetails userDetails = loadUser(username);
        if (!userDetails.isEnabled()) {
            throw new RuntimeException("该账户已被禁用");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new RuntimeException("该账户已被锁定");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new RuntimeException("该账号已过期");
        }
        return userDetails;
    }

    private SysUserDetails loadUser(String username) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        return SysUserDetails.builder()
                .userId(1L)
                .username(username)
                .enabled(true)
                .authorities(authorities)
                .password(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + new BCryptPasswordEncoder().encode("1234567")).build();
    }

    @Override
    public SysUserDetails loadUserByMobile(long phone) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        return SysUserDetails.builder()
                .userId(1L)
                .username(String.valueOf(phone))
                .enabled(true)
                .authorities(authorities)
                .password(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + new BCryptPasswordEncoder().encode("1234567")).build();

    }
}
