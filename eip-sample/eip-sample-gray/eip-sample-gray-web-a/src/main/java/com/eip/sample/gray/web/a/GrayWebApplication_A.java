package com.eip.sample.gray.web.a;

import com.eip.common.gray.core.handler.GrayServiceHandlerChain;
import com.eip.common.gray.feign.eureka.handle.RequestHeaderGrayServiceHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GrayWebApplication_A {

    public static void main(String[] args) {
        SpringApplication.run(GrayWebApplication_A.class, args);
    }

    @Bean
    GrayServiceHandlerChain grayServiceHandlerChaine(){
        GrayServiceHandlerChain chain = new GrayServiceHandlerChain();
        chain.addHandler(new RequestHeaderGrayServiceHandler());
        return chain;
    }
}