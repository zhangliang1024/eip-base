package com.eip.common.auth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * 配置用户
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //从数据库中查询用户信息
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 配置安全拦截策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用表单登录，前后端分离用不上
        //http.formLogin().disable()
        http
            .authorizeRequests()
            //放行注销接口
            .antMatchers("/oauth/logout").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginProcessingUrl("/login").permitAll()
            .and()
            .csrf().disable(); //关闭csrf
    }


    /**
     * Function: 加密算法
     * <p>
     * Date: 2021/12/17 17:56
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * AuthenticationManager对象在OAuth2认证服务中要使用，提前放入IOC容器中
     * Oauth的密码模式需要
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
