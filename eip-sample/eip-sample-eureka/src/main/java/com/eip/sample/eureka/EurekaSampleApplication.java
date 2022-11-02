package com.eip.sample.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Date: 2022/11/2 18:15
 *
 * @author 张良
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaSampleApplication.class, args);
    }
}