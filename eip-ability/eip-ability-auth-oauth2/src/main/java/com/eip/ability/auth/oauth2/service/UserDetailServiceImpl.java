package com.eip.ability.auth.oauth2.service;

import com.eip.ability.auth.oauth2.feign.AdminFeignClient;
import com.eip.common.auth.core.domain.Result;
import com.eip.common.auth.core.domain.UserInfoDetails;
import com.eip.common.core.constants.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassName: UserDetailServiceImpl
 * Function:
 * Date: 2022年09月19 19:07:42
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminFeignClient adminFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Set<String> authorize = new HashSet<>();
        authorize.add("ROLE_ADMIN");
        String tenantCode = request.getHeader(AuthConstants.OAUTH2_HEARDE_TENANT_CODE);
        //UserInfoDetails build = UserInfoDetails.builder()
        //        .userId(100L)
        //        .username(username)
        //        .password(passwordEncoder.encode("123456"))
        //        .authorities(authorize.stream().filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()))
        //        .build();

        Result<UserInfoDetails> result = adminFeignClient.loadUserByUsername(username, tenantCode);
        System.out.println(result);
        UserInfoDetails build = result.getData();
        //data.setAuthorities(authorize.stream().filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        return build;
        //System.out.println(username);
        //return SecurityUser.builder()
        //        .userId(100L)
        //        .username(username)
        //        .password(passwordEncoder.encode("123456"))
        //        .authorities(authorize.stream().filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()))
        //        .build();

        //return User.withUsername(username)
        //        .password(passwordEncoder.encode("123456"))
        //        .authorities("ROLE_ADMIN")
        //        .build();
    }

    //public static void main(String[] args) {
    //    System.out.println(new BCryptPasswordEncoder().encode("123"));
    //}
}
