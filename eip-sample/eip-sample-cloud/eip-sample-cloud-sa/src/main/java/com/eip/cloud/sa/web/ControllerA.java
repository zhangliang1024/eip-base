package com.eip.cloud.sa.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ControllerA
 * Function:
 * Date: 2023年07月06 13:16:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RestController
@RequestMapping("sa")
public class ControllerA {

    @GetMapping("success/{time}")
    public String getSuccess(@PathVariable(value = "time") int sleepTime,boolean flag) {
        log.info("------ ControllerA getSuccess sleep {}ms , flag: {}-----",sleepTime,flag);
        if(flag){
            sleep(sleepTime);
        }
        return "ControllerA sleep " + sleepTime + "ms uccess";
    }

    public void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
