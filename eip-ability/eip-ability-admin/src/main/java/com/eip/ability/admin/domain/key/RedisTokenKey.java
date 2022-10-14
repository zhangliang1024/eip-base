package com.eip.ability.admin.domain.key;

import com.eip.ability.admin.util.StringUtils;
import org.springframework.stereotype.Component;

/**
 * ClassName: TokenRedisKey
 * Function:
 * Date: 2022年09月30 17:47:04
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class RedisTokenKey extends DefaultRedisKey {

    public static final String BASE_TOKEN = "TOKEN";
    public static final String ACCESS_TOKEN = "ACCESS";
    public static final String REFRESH_TOKEN = "REFRESH";


    @Override
    public String prfix() {
        return super.appName() + StringUtils.SIGN_MAO_HAO + BASE_TOKEN + StringUtils.SIGN_MAO_HAO;
    }

    public String getAccessToken(String key) {
        return prfix() + ACCESS_TOKEN + StringUtils.SIGN_MAO_HAO + key;
    }

    public String getRefreshToken(String key) {
        return prfix() + REFRESH_TOKEN + StringUtils.SIGN_MAO_HAO + key;
    }

}
