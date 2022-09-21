package com.eip.ability.admin.service;


import com.eip.ability.admin.domain.entity.baseinfo.UserRole;
import com.eip.ability.admin.domain.vo.UserRoleResp;
import com.eip.ability.admin.mybatis.supers.SuperService;

/**
 * <p>
 * 业务接口
 * 角色分配
 * 账号角色绑定
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
public interface UserRoleService extends SuperService<UserRole> {

    /**
     * 根据劫色查询用户
     *
     * @param roleId 角色id
     * @return 查询结果
     */
    UserRoleResp findUserByRoleId(Long roleId);

}
