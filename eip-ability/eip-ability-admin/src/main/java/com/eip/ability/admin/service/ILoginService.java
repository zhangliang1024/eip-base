package com.eip.ability.admin.service;

import com.eip.ability.admin.domain.dto.LoginDTO;
import com.eip.ability.admin.domain.vo.LoginVO;

/**
 * ClassName: ILoginService
 * Function:
 * Date: 2022年09月28 11:02:47
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface ILoginService {

    LoginVO login(LoginDTO loginDTO);

    void logout(LoginDTO loginDTO);
}
