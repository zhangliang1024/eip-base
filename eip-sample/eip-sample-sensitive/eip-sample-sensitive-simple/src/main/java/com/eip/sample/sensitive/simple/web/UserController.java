package com.eip.sample.sensitive.simple.web;

import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.sample.sensitive.simple.domain.User;
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
public class UserController {

    @GetMapping("sensitive")
    public ApiResult getUser() {
        return ApiResult.ok(User.mock("李思思"));
    }

}
