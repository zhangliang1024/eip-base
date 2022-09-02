package com.eip.cloud.eip.common.sensitive.custom.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: UserController
 * Function:
 * Date: 2022年07月21 13:44:53
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping("one")
    public User getUser(){
        return User.builder()
                .userId(1111)
                .userName("张三")
                .email("xxxx@124.com")
                .remark("你就说吧要则呢么半")
                .build();
    }

}
