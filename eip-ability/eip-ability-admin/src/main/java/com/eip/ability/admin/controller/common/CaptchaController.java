package com.eip.ability.admin.controller.common;

import com.eip.ability.admin.oauth2.IgnoreAuthorize;
import com.eip.ability.admin.service.VerificationService;
import com.wf.captcha.base.Captcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Levin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/captcha")
@Tag(name = "验证码", description = "验证码")
@RequiredArgsConstructor
public class CaptchaController {

    private final VerificationService verificationService;

    @SneakyThrows
    @IgnoreAuthorize
    @GetMapping(produces = "image/png")
    @Operation(description = "验证码 - [DONE] - [Levin]")
    public void create(@RequestParam(value = "key") String key, HttpServletResponse response) {
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        final Captcha captcha = verificationService.create(key);
        captcha.out(response.getOutputStream());
    }


}
