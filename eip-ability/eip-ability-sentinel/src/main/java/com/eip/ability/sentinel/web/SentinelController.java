package com.eip.ability.sentinel.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SentinelController
 * Function:
 * Date: 2022年01月24 10:21:27
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
@RequestMapping("/base/api")
@CrossOrigin
public class SentinelController {


    @SentinelResource(value = "hello-flow",blockHandler = "sayHelloBlockHandler")
    @RequestMapping("/hello")
    public String sayHello() {
        return "hello Wold";
    }

    public String sayHelloBlockHandler() {
        return "say hello block advice";
    }

}
