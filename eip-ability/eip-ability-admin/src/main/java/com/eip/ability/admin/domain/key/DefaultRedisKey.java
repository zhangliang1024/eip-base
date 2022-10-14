package com.eip.ability.admin.domain.key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ClassName: DefaultRedisKey
 * Function:
 * Date: 2022年09月30 18:21:12
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public abstract class DefaultRedisKey implements RedisKey {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public String appName() {
        return applicationName;
    }

}
