package com.eip.sample.gray.web.d.feign.impl;

import com.eip.sample.gray.web.d.feign.IDemoFeign;
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
