package com.eip.sample.gray.web.c.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 在b服务中定义访问a服务的feignclient, 实际生产开发时考虑由a服务定义并直接给出依赖
 */
@FeignClient(name = "service-d") // name为服务在注册中心的名字s
public interface IDemoFeign {

    @GetMapping("/demo/hello")
    String hello();

}

