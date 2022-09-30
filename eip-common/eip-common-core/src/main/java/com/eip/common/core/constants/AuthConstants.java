package com.eip.common.core.constants;

/**
 * ClassName: AuthConstants
 * Function:
 * Date: 2022年01月18 14:49:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AuthConstants {

    public static final String LOGIN_VAL_ATTRIBUTE = "loginVal_attribute";

    /**
     * JWT的秘钥
     * TODO 实际项目中需要统一配置到配置文件中，资源服务也需要用到
     */
    public static final String SIGN_KEY = "myjszl";

    public static final String TOKEN_NAME = "jwt-token";

    public static final String PRINCIPAL_NAME = "principal";

    public static final String AUTHORITIES_NAME = "authorities";

    public static final String JTI = "jti";

    public static final String EXPR = "expr";


    /**
     * 权限<->url对应的KEY
     */
    public static final String OAUTH_URLS = "oauth2:oauth_urls";

    /**
     * JWT令牌黑名单的KEY
     */
    public static final String JTI_KEY_PREFIX = "oauth2:black:";

    /**
     * 角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE_";

    public static final String METHOD_SUFFIX = ":";

    public static final String ROLE_ROOT_CODE = "ROLE_ROOT";


    public static final String CONTEXT_KEY_USER_ID = "userId";
    public static final String CONTEXT_KEY_USERNAME = "userName";
    public static final String CONTEXT_KEY_USER_NAME = "user";
    public static final String CONTEXT_KEY_USER_TOKEN = "userToken";


    /**
     * 签名 类型
     */
    public static final String ENCRYPT_TYPE = "RSA";

    /**
     * JWT秘钥，实际项目中需要统一配置到文件中，资源服务也需要用到
     * JWT签名使用的是对称加密，资源服务器需使用相同的秘钥进行校验和解析
     * 实际工作中 使用非对称加密方式更安全
     */
    public static final String OAUTH2_SING_KEY = "eip-oauth2-key";

    /**
     * JWT token增强字段
     */
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String TENANT_CODE = "tenant_code";
    public static final String TENANT_ID = "tenant_id";

    public static final String OAUTH2_HEARDE_TENANT_CODE = "Tenant-Code";
    public static final String OAUTH2_USERNAME = "username";
    public static final String OAUTH2_PASSWORD = "password";
    public static final String OAUTH2_GRANT_TYPE = "grant_type";

    public static final String OAUTH2_PASSWORD_GRANT_TYPE = "password";

    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "client_credentials";


    /**
     * 刷新tokne过期时间: default 30 days
     */
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;

    /**
     * access tokne过期时间: default 2 hours
     */
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 2;


}
