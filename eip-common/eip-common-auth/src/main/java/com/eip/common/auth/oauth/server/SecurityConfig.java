package com.eip.common.auth.oauth.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ClassName: SecurityConfig
 * Function: Spring Security安全配置
 * Date: 2021年12月17 17:55:51
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 配置用户
     * TODO 这里为方便先保存内存
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("amdin")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("admin")
                .and()
                .withUser("user")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("user");
    }

    /**
     * 配置安全拦截策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.authorizeRequests()
               .anyRequest().authenticated()
               .and()
               .formLogin()
               .loginProcessingUrl("/login")
               .permitAll()
               .and()
               .csrf()
               .disable();
    }


    /**
     * Function: 加密算法
     * <p>
     * Date: 2021/12/17 17:56
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 注入认证个管理器 AuthenticationManager
     * Oauth的密码模式需要
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
