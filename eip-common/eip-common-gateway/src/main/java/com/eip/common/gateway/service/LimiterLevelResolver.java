package com.eip.common.gateway.service;

import com.eip.common.gateway.model.RateLimiterLevel;

/**
 * ClassName: LimiterLevelResolver
 * Function:
 * Date: 2022年01月19 09:56:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface LimiterLevelResolver {

    default void save(RateLimiterLevel limiterLevel){}

    default RateLimiterLevel get(){
        return null;
    }

}
