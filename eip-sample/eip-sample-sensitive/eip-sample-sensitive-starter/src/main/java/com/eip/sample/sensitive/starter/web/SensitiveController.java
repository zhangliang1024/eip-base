package com.eip.sample.sensitive.starter.web;

import com.eip.cloud.eip.common.sensitive.annotation.IgnoreSensitive;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.sample.sensitive.starter.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SensitiveController
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
public class SensitiveController {


    @GetMapping("sensitive")
    public ApiResult sensitive() {
        return ApiResult.ok(User.mock("张三三","1234456"));
    }

    @IgnoreSensitive
    @GetMapping("sensitive1")
    public ApiResult sample4() {
        return ApiResult.ok(User.mock("李思思","1234456"));
    }

}
