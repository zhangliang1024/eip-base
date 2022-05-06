package com.eip.common.gateway.constants;

/**
 * ClassName: GatawayConstant
 * Function:
 * Date: 2022年01月19 10:02:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface GatawayConstant {

    /**
     * 限流缓存key
     */
    String REDIS_LIMIT_KEY = "ms_redis_limit_key:";
    /**
     * 默认限流等级
     */
    String DEFAULT_LEVEL = "1";

    /**
     * 默认限流 流速 与 桶大小
     */
    int DEFAULT_LIMIT_LEVEL = 10;

    /**
     * 默认单位 1:秒，2:分钟，3:小时，4:天
     */
    int DEFAULT_LIMIT_TYPE = 1;

}
