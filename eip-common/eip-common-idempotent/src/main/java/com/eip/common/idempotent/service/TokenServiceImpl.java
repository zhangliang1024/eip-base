package com.eip.common.idempotent.service;

import com.eip.common.idempotent.config.AutoIdemProperties;
import com.eip.common.idempotent.exception.AutoIdemResponseEnum;
import com.eip.common.idempotent.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * ClassName: TokenServiceImpl
 * Function: token处理
 * Date: 2021年12月07 15:56:06
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class TokenServiceImpl implements TokenService {

    private final RedisService redisService;

    private final AutoIdemProperties idemProperties;

    @Override
    public String createToken() {
        String token = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        //StringBuilder sb = new StringBuilder();
        //if (StringUtils.isNotBlank(idemProperties.getPrefixServerName())) {
        //    sb.append(IDEM_TOKEN).append(":").append(idemProperties.getPrefixServerName()).append(":");
        //} else {
        //    sb.append(IDEM_TOKEN).append(":").append(DEFAULT_PREFIX).append(":");
        //}
        //String key = sb.append(token).toString();
        String key = getRedisKey(token);
        redisService.set(key, token, idemProperties.getExpireTime());
        return token;
    }

    @Override
    public boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader(IDEM_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(IDEM_TOKEN);
            //请求未携带token
            AutoIdemResponseEnum.REQUEST_TOKEN_IS_NULL.assertNotEmpty(token);
            String key = getRedisKey(token);
            //token不存在，则重复请求
            boolean exists = redisService.exists(key);
            AutoIdemResponseEnum.REQUEST_REPEAT.assertIsTrue(exists);

            boolean delete = redisService.remove(token);
            AutoIdemResponseEnum.OPERATE_EXCEPTION.assertIsTrue(delete);
        }
        return true;
    }

    @Override
    public String getPrefixServerName() {
        if (StringUtils.isNotBlank(idemProperties.getPrefixServerName())) {
           return idemProperties.getPrefixServerName();
        }
        return DEFAULT_PREFIX;
    }
}
