package com.eip.sample.oauth2.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: AuthWebApplication
 * Function:
 * Date: 2022年01月19 10:26:51
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.eip.ability.auth.web","com.eip.common.auth"})
public class AuthWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthWebApplication.class,args);
    }

}
