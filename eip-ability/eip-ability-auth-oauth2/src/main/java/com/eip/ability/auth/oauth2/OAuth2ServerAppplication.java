package com.eip.ability.auth.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * ClassName: OAuth2ServerAppplication
 * Function: 权鉴认证
 * Date: 2022年09月19 13:14:46
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@EnableFeignClients("com.eip.ability.auth.oauth2.feign")
@MapperScan(value = "com.eip.**.mapper", annotationClass = Repository.class)
@EnableDiscoveryClient
@SpringBootApplication
public class OAuth2ServerAppplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2ServerAppplication.class, args);
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

}