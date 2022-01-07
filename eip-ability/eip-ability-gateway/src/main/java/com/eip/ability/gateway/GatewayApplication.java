package com.eip.ability.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName: GatewayApplication
 * Function:
 * Date: 2022年01月07 14:41:18
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
