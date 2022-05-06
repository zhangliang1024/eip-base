package com.eip.ability.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ClassName: WebConfiguration
 * Function: Spring Security 安全配置
 * Date: 2022年01月17 16:33:15
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 这里演示，暂定从内存中加载用户数据。实际从数据库加载
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("admin")
                .and()
                .withUser("user")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("user");
    }

    /***
     * 配置安全拦截策略
     * 由于需要授权码验证模式，因此开启表单提交模式，所有url都需要验证
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()//所有url都需要校验
                .and()
                .formLogin()//允许表单登录
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .csrf()
                .disable();
    }


    /**
     * 注入认证管理器 AuthenticationManager在密码授权模式下会用到，这里提前注入。
     * 若不是密码模式，可以不注入
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密算法
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
