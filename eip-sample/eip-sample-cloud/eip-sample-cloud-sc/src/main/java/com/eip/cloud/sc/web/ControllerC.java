package com.eip.cloud.sc.web;

import com.eip.cloud.sc.feign.FeignServiceB;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("sc")
public class ControllerC {

    private final FeignServiceB feignServiceB;

    @GetMapping("success/{time}")
    public String getSuccess(@PathVariable(value = "time") int sleepTime, boolean flag) {
        log.info("------ ControllerC getSuccess sleep {}ms , flag: {}-----", sleepTime, flag);
        if (flag) {
            sleep(sleepTime);
        }
        return feignServiceB.getSuccess(sleepTime, flag);
    }

    @GetMapping("fail/{time}")
    public String getFail(@PathVariable(value = "time") int sleepTime, boolean flag) {
        log.info("------ ControllerC getFail sleep {}ms , flag: {}-----", sleepTime, flag);
        if (flag) {
            // int i = 1/0;
            throw new RuntimeException("ControllerC error");
        }
        return feignServiceB.getSuccess(sleepTime, flag);
    }


    public void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
