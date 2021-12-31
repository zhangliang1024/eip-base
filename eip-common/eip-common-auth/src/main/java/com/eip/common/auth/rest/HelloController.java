package com.eip.common.auth.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: HelloController
 * Function:
 * Date: 2021年12月17 19:04:14
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
public class HelloController {


    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_admin')")
    public String admin(){
        return "admin";
    }

}
