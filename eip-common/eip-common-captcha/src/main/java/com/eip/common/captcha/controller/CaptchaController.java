package com.eip.common.captcha.controller;

import com.eip.common.captcha.model.CaptachVO;
import com.eip.common.core.constants.GlobalConstans;
import com.eip.common.core.constants.RedisKeyConstants;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.core.service.RedisService;
import com.eip.common.core.utils.encrypt.Base64Util;
import com.eip.common.core.utils.uid.UUIDUtil;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayOutputStream;

/**
 * ClassName: CaptchaController
 * Function:
 * Date: 2022年01月26 10:11:19
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
public class CaptchaController {


    @Autowired
    private RedisService redisService;


    @GetMapping("/captcha/image")
    public void captchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        // 验证码
        String captchaCode = specCaptcha.text().toLowerCase();
        // 验证码存入redis
        redisService.set(String.format(RedisKeyConstants.ADMIN_SERVER_CAPTCHA_PREFIX, captchaCode),
                captchaCode,
                GlobalConstans.LOGIN_CAPTCHA_EXPIRATION);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    @GetMapping("/captcha/code")
    public ApiResult captchaCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        // 验证码
        String captchaCode = specCaptcha.text().toLowerCase();
        String uid = UUIDUtil.generateShortUuid();
        // 验证码存入redis
        redisService.set(String.format(RedisKeyConstants.ADMIN_SERVER_CAPTCHA_PREFIX, uid),
                captchaCode,
                GlobalConstans.LOGIN_CAPTCHA_EXPIRATION);
        // 输出图片流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        specCaptcha.out(stream);
        String captcha = Base64Util.byteArrayToBase64(stream.toByteArray());
        return ApiResult.ok(CaptachVO.builder()
                .captach(captcha)
                .uid(uid)
                .build());
    }

}
