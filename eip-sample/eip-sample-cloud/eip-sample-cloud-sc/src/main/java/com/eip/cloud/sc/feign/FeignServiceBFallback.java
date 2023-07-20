package com.eip.cloud.sc.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: FeignServiceBFallback
 * Function:
 * Date: 2023年07月06 16:37:37
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class FeignServiceBFallback implements FeignServiceB {

    @Override
    public String getSuccess(int sleepTime, boolean flag) {
        log.info("------  FeignServiceBFallback  -----");
        return "FeignServiceC - Fallback";
    }
}
