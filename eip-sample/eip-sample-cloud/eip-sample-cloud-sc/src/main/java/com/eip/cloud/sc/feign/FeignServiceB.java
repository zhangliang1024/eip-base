package com.eip.cloud.sc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: FeignServiceB
 * Function:
 * Date: 2023年07月06 13:21:07
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@FeignClient(name = "service-b", fallback = FeignServiceBFallback.class, configuration = FeignConfiguration.class)
public interface FeignServiceB {

    @GetMapping("sb/success/{time}")
    String getSuccess(@PathVariable(value = "time") int sleepTime, @RequestParam(name = "flag") boolean flag);
}
