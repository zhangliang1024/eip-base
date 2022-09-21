package com.eip.ability.admin.service;

import com.eip.ability.admin.domain.entity.baseinfo.RoleOrg;
import com.eip.ability.admin.mybatis.supers.SuperService;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 角色组织关系
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
public interface RoleOrgService extends SuperService<RoleOrg> {

    /**
     * 根据角色id查询
     *
     * @param id
     * @return
     */
    List<Long> listOrgByRoleId(Long id);
}
