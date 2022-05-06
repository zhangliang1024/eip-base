package com.eip.common.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: RateLimiterVO
 * Function: 限流配置表
 * Date: 2022年01月19 09:58:03
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateLimiterVO implements Serializable {


    private int limitId;
    /**
     * 等极
     **/
    private String level;
    /**
     * 等极名称（描述）
     **/
    private String levelName;
    /**
     * 流速
     **/
    private int replenishRate;
    /**
     * 桶容量
     **/
    private int burstCapacity;
    /**
     * 单位 1:秒，2:分钟，3:小时，4:天
     **/
    private int limitType;

}
