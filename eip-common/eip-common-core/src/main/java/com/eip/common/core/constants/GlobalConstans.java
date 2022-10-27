package com.eip.common.core.constants;

/**
 * ClassName: GlobalConstans
 * Function:
 * Date: 2022年01月17 14:38:36
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface GlobalConstans {

    ThreadLocal<String> GLOBAL_TRACE = new ThreadLocal();


    String TRACE_LOG_ID = "tid";


    //全局trace-id
    String GLOBAL_TRACE_ID = "global_trace_id";

    /**
     * 验证码有效期：2分钟
     */
    Integer LOGIN_CAPTCHA_EXPIRATION = 120;

    /**
     * 接口请求hearder增加token
     */
    String AUTH_TOKEN = "Auth-Token";

    /**
     *
     */
    String URL_PERM_ROLES_KEY = "system:perm_roles_rule:url";

    /**
     * actuator端点入口
     */
    String BASE_ACTUATOR_URL = "/actuator";

}
