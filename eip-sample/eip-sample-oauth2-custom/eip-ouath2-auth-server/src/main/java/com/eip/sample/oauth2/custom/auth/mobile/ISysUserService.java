package com.eip.sample.oauth2.custom.auth.mobile;

import com.eip.sample.oauth2.custom.auth.system.SysUserDetails;

/**
 * ClassName: ISysUserService
 * Function:
 * Date: 2022年09月09 14:53:39
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface ISysUserService {


    /**
     * 根据手机号查询用户详情
     */
    SysUserDetails loadUserByMobile(long phone);

}
