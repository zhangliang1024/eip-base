package com.eip.base.business.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 11:31
 * @version：V1.0
 */
@SpringBootApplication
@ComponentScan({"com.eip.base.*"})
public class BussinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BussinessApplication.class,args);
    }
}
