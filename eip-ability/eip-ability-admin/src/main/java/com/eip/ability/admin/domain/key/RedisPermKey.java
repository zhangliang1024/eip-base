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
public class RedisPermKey extends DefaultRedisKey {

    public static final String BASE_PERM = "PERMISSIONS";


    @Override
    public String prfix() {
        return super.appName() + StringUtils.SIGN_MAO_HAO + BASE_PERM + StringUtils.SIGN_MAO_HAO;
    }

    public String getPermKey(Object key) {
        return prfix() + String.valueOf(key);
    }


}
