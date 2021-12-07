package com.eip.common.idempotent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: AutoIdemProperties
 * Function:
 * Date: 2021年12月07 17:46:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(prefix = "eip.auto-idem")
public class AutoIdemProperties {

    /**
     * Function: 是否启用幂等拦截
     * <p>
     * Date: 2021/12/7 17:48
     *
     * @author 张良
     */
    private boolean enabled = true;

    /**
     * Function: 业务前缀
     * <p>
     * Date: 2021/12/7 18:06
     *
     * @author 张良
     */
    private String prefixServerName;

    /**
     * Function: 幂等过期时间
     * <p>       默认1秒
     * Date: 2021/12/7 17:49
     *
     * @author 张良
     */
    private long expireTime = 30000L;
}
