package com.eip.common.idempotent.service;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: TokenService
 * Function: token处理
 * Date: 2021年12月07 15:54:14
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface TokenService {

    /**
     * 幂等token标识
     */
    String IDEM_TOKEN = "IDEM_TOKEN";

    /**
     * 默认业务标识前缀
     */
    String DEFAULT_PREFIX = "DEFAULT_SERVER";

    /**
     * Function: 创建token
     * <p>
     * Date: 2021/12/7 15:55
     *
     * @author 张良
     */
    String createToken();


    /**
     * Function: 校验token
     * <p>
     * Date: 2021/12/7 15:55
     *
     * @author 张良
     */
    boolean checkToken(HttpServletRequest request);


    /**
     * Function: 获取业务前缀
     * <p>
     * Date: 2021/12/7 19:10
     *
     * @author 张良
     */
    String getPrefixServerName();


    /**
     * Function: 获取缓存key
     * <p>
     * Date: 2021/12/7 19:10
     *
     * @author 张良
     */
    default String getRedisKey(String token){
        StringBuilder sb = new StringBuilder();
        sb.append(IDEM_TOKEN).append(":").append(getPrefixServerName()).append(":");
        String key = sb.append(token).toString();
        return key;
    }


}
