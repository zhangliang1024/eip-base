package com.eip.common.core.constants;

/**
 * ClassName: RedisKeyConstants
 * Function:
 * Date: 2022年01月26 11:26:46
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface RedisKeyConstants {

    String REDIS_KEY_GLOBAL_PREFIX = "%s:global:%s";

    String REDIS_KEY_CAPTCHA_PREFIX = "%s:captcha:";

    String ADMIN_SERVER_CAPTCHA_PREFIX = String.format(REDIS_KEY_CAPTCHA_PREFIX,ServiceConstants.ADMIN_SERVER) + "%s";

}
