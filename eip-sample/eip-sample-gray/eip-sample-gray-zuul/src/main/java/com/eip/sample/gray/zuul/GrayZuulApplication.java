package com.eip.sample.gray.zuul;

import com.eip.common.gray.core.handler.GrayServiceHandlerChain;
import com.eip.common.gray.zuul.RequestHeaderGrayServiceHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class GrayZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrayZuulApplication.class, args);
    }

    @Bean
    GrayServiceHandlerChain grayServiceHandlerChaine() {
        GrayServiceHandlerChain chain = new GrayServiceHandlerChain();
        chain.addHandler(new RequestHeaderGrayServiceHandler());
        return chain;
    }
}
