package com.eip.common.core.constants;

/**
 * ClassName: AuthConstants
 * Function:
 * Date: 2022年01月18 14:49:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface AuthConstants {

    String LOGIN_VAL_ATTRIBUTE = "loginVal_attribute";

    /**
     * JWT的秘钥
     * TODO 实际项目中需要统一配置到配置文件中，资源服务也需要用到
     */
    String SIGN_KEY = "myjszl";

    String TOKEN_NAME = "jwt-token";

    String PRINCIPAL_NAME = "principal";

    String AUTHORITIES_NAME = "authorities";

    String USER_ID = "user_id";

    String JTI = "jti";

    String EXPR = "expr";


    /**
     * 权限<->url对应的KEY
     */
    String OAUTH_URLS = "oauth2:oauth_urls";

    /**
     * JWT令牌黑名单的KEY
     */
    String JTI_KEY_PREFIX = "oauth2:black:";

    /**
     * 角色前缀
     */
    String ROLE_PREFIX = "ROLE_";

    String METHOD_SUFFIX = ":";

    String ROLE_ROOT_CODE = "ROLE_ROOT";


    String CONTEXT_KEY_USER_ID = "userId";
    String CONTEXT_KEY_USERNAME = "userName";
    String CONTEXT_KEY_USER_NAME = "user";
    String CONTEXT_KEY_USER_TOKEN = "userToken";

}
