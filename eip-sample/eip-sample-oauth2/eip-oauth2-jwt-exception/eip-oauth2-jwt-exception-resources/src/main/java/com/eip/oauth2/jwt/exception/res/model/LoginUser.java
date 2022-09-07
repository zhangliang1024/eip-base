package com.eip.oauth2.jwt.exception.res.model;

import lombok.Data;

import java.util.List;

/**
 * ClassName: LoginUser
 * Function: 保存登录用户的信息，此处可以根据业务需要扩展
 * Date: 2022年01月18 15:59:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class LoginUser extends JwtInfomation {

    private String userId;

    private String username;

    private List<String> authorities;

}
