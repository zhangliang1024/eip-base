package com.eip.ability.admin.controller;

import lombok.Data;

/**
 * ClassName: TokenInfo
 * Function:
 * Date: 2022年09月28 11:22:42
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class TokenInfo {

    private Long user_id;
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String user_name;
    private Long expires_in;
    private String scope;

}