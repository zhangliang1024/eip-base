package com.eip.ability.auth.oauth2.web;

import com.eip.ability.auth.oauth2.feign.AdminFeignClient;
import com.eip.common.auth.core.domain.Result;
import com.eip.common.auth.core.domain.UserInfoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: Controller
 * Function:
 * Date: 2022年09月28 16:15:42
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
public class Controller {

    @Autowired
    private AdminFeignClient feignClient;


    @GetMapping("/feign")
    public Result<UserInfoDetails> feign(){
        Result<UserInfoDetails> result = feignClient.loadUserByUsername("admin","0000");
        return result;
    }
}
