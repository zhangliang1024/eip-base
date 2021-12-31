package com.eip.ability.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * ClassName: EurekaApplication
 * Function:
 * Date: 2021年12月14 17:28:15
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class,args);
    }
}
