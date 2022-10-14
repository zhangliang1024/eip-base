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
public class RedisUserKey extends DefaultRedisKey {

    public static final String BASE_USER = "USER";
    public static final String USER_INFO = "INFO";


    @Override
    public String prfix() {
        return super.appName() + StringUtils.SIGN_MAO_HAO + BASE_USER + StringUtils.SIGN_MAO_HAO;
    }

    public String getUserInfoKey(Object key) {
        return prfix() + USER_INFO + StringUtils.SIGN_MAO_HAO + String.valueOf(key);
    }


}
