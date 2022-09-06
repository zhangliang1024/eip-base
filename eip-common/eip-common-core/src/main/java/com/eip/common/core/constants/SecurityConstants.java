package com.eip.common.core.constants;

/**
 * ClassName: SecurityConstants
 * Function: OAuth2 认证相关
 * Date: 2022年09月05 15:24:21
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface SecurityConstants {

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";
    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "Bearer ";
    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";
    /**
     * JWT ID 唯一标识
     */
    String JWT_JTI= "jti";
    /**
     * JWT 过期时间
     */
    String JWX_EXP = "exp" ;
    /**
     * JWT 存取权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";
    /**
     * jwt存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";
    /**
     *
     */
    String APP_api_pattern = "/*/app-api/**";
    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_RREFIX = "auth:token:blackList";



}
