package com.eip.sample.common.controller;

import com.eip.common.captcha.domian.CaptachVO;
import com.eip.common.core.constants.GlobalConstans;
import com.eip.common.core.constants.RedisKeyConstants;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.core.redis.RedisService;
import com.eip.common.core.utils.encrypt.Base64Util;
import com.eip.common.core.utils.uid.UUIDUtil;
//import com.pig4cloud.captcha.ArithmeticCaptcha;
//import com.pig4cloud.captcha.SpecCaptcha;
//import com.pig4cloud.captcha.base.Captcha;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        String key = String.format(RedisKeyConstants.ADMIN_SERVER_CAPTCHA_PREFIX, captchaCode);
        redisService.set(key, captchaCode, GlobalConstans.LOGIN_CAPTCHA_EXPIRATION);
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
        String key = String.format(RedisKeyConstants.ADMIN_SERVER_CAPTCHA_PREFIX, uid);
        redisService.set(key, captchaCode, GlobalConstans.LOGIN_CAPTCHA_EXPIRATION);
        // 输出图片流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        specCaptcha.out(stream);
        String captcha = Base64Util.byteArrayToBase64(stream.toByteArray());
        return ApiResult.ok(CaptachVO.builder().captach(captcha).uid(uid).build());
    }

    @GetMapping("/captcha/arithmetic")
    public void captchaCode11(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);

        // 算术类型
        //1.方式一
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setCharType(2);

        //2.方式二
        //captcha.setLen(2);  // 几位数运算，默认是两位
        //captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        //captcha.text();  // 获取运算的结果：5
        //captcha.supportAlgorithmSign(2); // 可设置支持的算法：2 表示只生成带加减法的公式
        //captcha.setDifficulty(9); // 设置计算难度，参与计算的每一个整数的最大值

        // 验证码存入redis
        String uid = UUIDUtil.generateShortUuid();
        String captchaCode = captcha.text().toLowerCase();
        String key = String.format(RedisKeyConstants.ADMIN_SERVER_CAPTCHA_PREFIX, uid);
        redisService.set(key, captchaCode, GlobalConstans.LOGIN_CAPTCHA_EXPIRATION);

        captcha.out(response.getOutputStream());  // 输出验证码
        //简单算术类型 SimpleArithmeticCaptcha,用法同ArithmeticCaptcha,只支持加减，计算结果为正整数

    }

}
