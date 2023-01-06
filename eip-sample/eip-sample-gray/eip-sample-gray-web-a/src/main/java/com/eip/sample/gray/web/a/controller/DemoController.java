package com.eip.sample.gray.web.a.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/")
public class DemoController {

    @Value("${eureka.instance.metadata-map.grayVersion}")
    public String lancher;

    @GetMapping("hello")
    public String hello() {
        return lancher;
    }
}
