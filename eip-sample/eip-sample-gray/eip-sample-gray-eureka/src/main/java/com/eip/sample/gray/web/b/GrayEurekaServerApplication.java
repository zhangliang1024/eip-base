package com.eip.sample.gray.web.b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class GrayEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrayEurekaServerApplication.class, args);
    }
}