package com.eip.cloud.sb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@EnableHystrix //网关Zuul不加此注解，也可以生效。具体微服务必须加此注解，否则无法暴露hystrix.stream端点
@EnableFeignClients
@SpringBootApplication
public class ServiceBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBApplication.class, args);
        log.info("-----------GatewayApplication success ----------");
    }

}