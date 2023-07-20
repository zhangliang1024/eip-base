package com.eip.cloud.mn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@Slf4j
@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication
public class DashboradApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboradApplication.class, args);
        log.info("-----------DashboradApplication success ----------");
    }

}