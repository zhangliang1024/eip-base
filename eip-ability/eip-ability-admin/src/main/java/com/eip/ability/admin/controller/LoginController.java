package com.eip.ability.admin.controller;

import com.eip.ability.admin.domain.dto.LoginDTO;
import com.eip.ability.admin.domain.vo.LoginVO;
import com.eip.ability.admin.service.ILoginService;
import com.eip.common.core.core.protocol.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: LoginController
 * Function:
 * Date: 2022年09月28 10:48:09
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RestController
@Tag(name = "登录登出", description = "登录登出管理")
@RequestMapping("admin")
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    @PostMapping("login")
    @Operation(summary = "用户登录")
    public ApiResult<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        LoginVO result = loginService.login(loginDTO);
        return ApiResult.success(result);
    }

    @PostMapping("logout")
    @Operation(summary = "用户退出")
    public ApiResult logout(@RequestBody LoginDTO loginDTO) {
        loginService.logout(loginDTO);
        return ApiResult.success();
    }


}
