package com.eip.common.gateway.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eip.common.gateway.constants.GatawayConstant;
import com.eip.common.gateway.model.RateLimiterLevel;
import com.eip.common.gateway.model.RateLimiterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DefaultLimiterLevelHandler
 * Function: 管理 limiter
 * Date: 2022年01月19 10:00:39
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class DefaultLimiterLevelHandler implements LimiterLevelResolver {


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(RateLimiterLevel limiterLevel) {
        redisTemplate.opsForValue().set(GatawayConstant.REDIS_LIMIT_KEY,limiterLevel);
    }

    @Override
    public RateLimiterLevel get() {
        RateLimiterLevel rateLimiterLevel = (RateLimiterLevel) redisTemplate.opsForValue().get(GatawayConstant.REDIS_LIMIT_KEY);
        //若缓存为空，则给予默认值
        if(ObjectUtil.isNull(rateLimiterLevel) || CollUtil.isEmpty(rateLimiterLevel.getLevels())){
            rateLimiterLevel = new RateLimiterLevel();
            List<RateLimiterVO> vos = new ArrayList<>();
            vos.add(RateLimiterVO
                    .builder()
                    .level(GatawayConstant.DEFAULT_LEVEL)
                    .burstCapacity(GatawayConstant.DEFAULT_LIMIT_LEVEL)
                    .replenishRate(GatawayConstant.DEFAULT_LIMIT_LEVEL)
                    .limitType(GatawayConstant.DEFAULT_LIMIT_TYPE)
                    .build());
            rateLimiterLevel.setLevels(vos);
        }
        return rateLimiterLevel;
    }
}
