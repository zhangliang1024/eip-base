package com.eip.ability.admin.configuration;

import com.eip.ability.admin.configuration.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Levin
 */
@Configuration
//@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 验证所有的请求
        http.authorizeRequests()
                //允许跨域请求的OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                // oauth 开头的不验证
                .antMatchers("/favicon.ico", "/druid", "/**", "/oauth/**", "/actuator/**", "/v2/api-docs").permitAll()
                .anyRequest().authenticated()
                // 关闭跨站请求防护及不使用session
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 自定义权限拒绝处理类
                .and();
    }
}
