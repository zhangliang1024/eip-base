package com.eip.base.business.web.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @项目名称：eip-base
 * @包名：com.eip.base.business.web.api
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 11:40
 * @version：V1.0
 */
@Tag(name = "test controller")
@RestController
@RequestMapping("")
public class TestController {


    @Operation(summary  = "test method")
    @GetMapping("test")
    public String test(HttpServletRequest request){
        String header = request.getHeader("x-auth-token");
        System.out.println("test swagger ...");
        return "success";
    }
}
