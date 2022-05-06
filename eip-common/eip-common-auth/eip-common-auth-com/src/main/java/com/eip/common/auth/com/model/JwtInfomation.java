package com.eip.common.auth.com.model;

import lombok.Data;

/**
 * ClassName: JwtInfomation
 * Function: JWT令牌相关的信息实体类
 * Date: 2022年01月18 15:58:15
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class JwtInfomation {


    /**
     * JWT令牌唯一ID
     */
    private String jwtId;

    /**
     * 过期时间，单位秒，距离过期时间还有多少秒
     */
    private Long expireIn;
}
