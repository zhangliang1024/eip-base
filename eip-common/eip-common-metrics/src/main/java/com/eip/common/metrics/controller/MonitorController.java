package com.eip.common.metrics.controller;

import com.eip.common.metrics.Metrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: MonitorController
 * Function:
 * Date: 2021年12月17 16:04:03
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
public class MonitorController {


    @GetMapping("test")
    @Metrics
    public String test() throws Exception{
        Thread.sleep(1000);
        System.out.println("=========== success  =================");
        return "success";
    }

}
