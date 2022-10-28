package com.eip.ability.auth.oauth2.web;

import com.eip.ability.auth.oauth2.feign.AdminFeignClient;
import com.eip.common.auth.core.domain.UserInfoDetails;
import com.eip.common.core.core.protocol.response.ApiResult;
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
    public ApiResult<UserInfoDetails> feign(){
        ApiResult<UserInfoDetails> result = feignClient.loadUserByUsername("admin","0000");
        return result;
    }
}
