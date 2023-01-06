package com.eip.sample.gray.web.d.controller;

import com.eip.sample.gray.web.d.feign.impl.DemoClinet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/")
public class DemoController {

    @Autowired
    private DemoClinet demoClinet;

    @Value("${eureka.instance.metadata-map.grayVersion}")
    public String lancher;

    @Value("${spring.application.name}")
    public String clientName;

    @GetMapping("hello")
    public String hello(){
        return clientName+":"+lancher+"\n"+demoClinet.hello();
    }
}
