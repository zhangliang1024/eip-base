package com.eip.common.gateway.model;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * ClassName: RateLimiterLevel
 * Function: 限流等级配置
 * Date: 2022年01月19 09:57:31
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class RateLimiterLevel implements Serializable {

    private List<RateLimiterVO> levels;
}
