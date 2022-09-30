package com.eip.ability.admin.service;

import com.eip.ability.admin.domain.entity.baseinfo.RoleApi;
import com.eip.ability.admin.domain.vo.ApiRoleResp;
import com.eip.ability.admin.mybatis.supers.SuperService;

import java.util.List;

/**
 * ClassName: IApiRoleService
 * Function:
 * Date: 2022年09月30 16:27:21
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface IApiRoleService extends SuperService<RoleApi> {


    ApiRoleResp findApiByRoleId(Long roleId);

    void saveApiRole(Long roleId, List<Long> apiIdList);
}
