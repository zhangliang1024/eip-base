package com.eip.sample.gray.web.b.feign.impl;

import com.eip.sample.gray.web.b.feign.IDemoFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoClinet implements IDemoFeign{

    @Autowired
    private IDemoFeign demoFeign;

    @Override
    public String hello() {
        String hello = demoFeign.hello();
        return hello;
    }
}
