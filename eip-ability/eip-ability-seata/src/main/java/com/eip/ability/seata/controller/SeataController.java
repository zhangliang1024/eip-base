package com.eip.ability.seata.controller;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SeataController
 * Function:
 * Date: 2022年01月25 10:48:53
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
public class SeataController {


    @GlobalTransactional
    @GetMapping("seata")
    public String hello(){
        return "hello seata";
    }

}
