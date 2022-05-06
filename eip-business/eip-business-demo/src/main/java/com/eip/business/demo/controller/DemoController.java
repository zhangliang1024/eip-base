package com.eip.business.demo.controller;

import com.eip.business.demo.config.TestProperteis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: DemoController
 * Function:
 * Date: 2022年01月10 17:50:21
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RestController
public class DemoController {


    @Autowired
    private TestProperteis properteis;

    @Value("${name}")
    private String name;

    @GetMapping("user")
    public String getResult(){
        log.info("[apollo-config] - name: {}",name);
        return "reponse : " + name;
    }

    @GetMapping("test")
    public String getProperteis(){
        log.info("[apollo-config] - properteis: {}",properteis);
        return properteis.toString();
    }
}
