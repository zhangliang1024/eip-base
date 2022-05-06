package com.eip.common.auth.server.web;

import com.eip.common.auth.client.util.AuthUserUtil;
import com.eip.common.core.constants.AuthConstants;
import com.eip.common.auth.com.model.LoginUser;
import com.eip.common.core.core.protocol.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: AuthController
 * Function:
 * Date: 2022年01月18 17:25:15
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RestController
public class AuthController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("oauth/logout")
    public ApiResult logout() {
        LoginUser loginVal = AuthUserUtil.getCurrentUser();
        log.info("令牌唯一ID：{},过期时间：{}", loginVal.getJwtId(), loginVal.getExpireIn());
        //这个jti放入redis中，并且过期时间设置为token的过期时间
        redisTemplate.opsForValue().set(AuthConstants.JTI_KEY_PREFIX + loginVal.getJwtId(), "", loginVal.getExpireIn(),
                TimeUnit.SECONDS);
        return new ApiResult("200", "注销成功");
    }

}
