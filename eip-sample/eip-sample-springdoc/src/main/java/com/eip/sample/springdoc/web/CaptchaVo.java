package com.eip.sample.springdoc.web;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Schema(description = "验证码")
public class CaptchaVo implements Serializable {


    @Schema(description = "uuid")
    private String uuid;
    @Schema(description = "验证码图片base64")
    private String base64;
}