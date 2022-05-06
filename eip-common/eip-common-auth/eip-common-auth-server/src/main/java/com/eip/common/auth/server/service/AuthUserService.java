package com.eip.common.auth.server.service;

import com.eip.common.auth.com.model.SecurityUser;

/**
 * ClassName: AuthUserService
 * Function:
 * Date: 2022年01月18 17:35:16
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface AuthUserService {

    SecurityUser loadUserByUsername(String username);
}
