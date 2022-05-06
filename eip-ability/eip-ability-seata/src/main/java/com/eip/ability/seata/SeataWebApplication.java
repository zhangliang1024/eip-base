package com.eip.ability.seata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName: SeataWebApplication
 * Function:
 * Date: 2022年01月25 10:44:59
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SeataWebApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.standalone", "true");
        SpringApplication.run(SeataWebApplication.class,args);
    }

}
