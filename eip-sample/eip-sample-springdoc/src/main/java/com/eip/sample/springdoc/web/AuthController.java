package com.eip.sample.springdoc.web;

import com.wf.captcha.SpecCaptcha;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "auth: 安全模块",description = "test:auth")
public class AuthController {


    @Operation(summary = "验证码")
    @GetMapping("captcha")
    public CaptchaVo captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String uuid = String.valueOf(RandomUtils.nextInt(10,10000));
        return new CaptchaVo(uuid, specCaptcha.toBase64());
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public void login() {

    }

    @Hidden
    @PostMapping("logout")
    public void logout() {

    }
}

