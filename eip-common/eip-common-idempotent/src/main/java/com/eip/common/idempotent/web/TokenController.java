package com.eip.common.idempotent.web;

import com.eip.common.idempotent.annotation.AutoIdempotent;
import com.eip.common.idempotent.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TokenController
 * Function: 创建token接口
 * Date: 2021年12月07 16:13:51
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
@RequestMapping("idem-token")
public class TokenController {


    @Autowired
    private TokenService tokenService;

    @GetMapping("create")
    public String createToken() {
        return tokenService.createToken();
    }

    @AutoIdempotent
    @GetMapping("check")
    public String checkToken() {
        return "check success";
    }

}
