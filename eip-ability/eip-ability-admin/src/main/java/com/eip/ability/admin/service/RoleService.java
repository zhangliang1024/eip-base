package com.eip.ability.admin.service;


import com.eip.ability.admin.domain.DataScope;
import com.eip.ability.admin.domain.dto.RoleDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.vo.RolePermissionResp;
import com.eip.ability.admin.mybatis.supers.SuperService;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 角色
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
public interface RoleService extends SuperService<Role> {


    /**
     * 根据 scope 查询角色
     *
     * @param scope scope
     * @return 查询结果
     */
    List<Role> list(DataScope scope);

    /**
     * 根据角色ID移除
     *
     * @param roleId roleId
     */
    void removeByRoleId(Long roleId);


    /**
     * 1，保存角色
     * 2，保存 与组织的关系
     *
     * @param data   data
     * @param userId 用户id
     */
    void saveRole(Long userId, RoleDTO data);

    /**
     * 修改角色
     *
     * @param role   role
     * @param userId userId
     * @param roleId 角色ID
     */
    void updateRole(Long roleId, Long userId, RoleDTO role);


    /**
     * 给角色分配用户
     *
     * @param roleId     roleId
     * @param userIdList userIdList
     */
    void saveUserRole(Long roleId, List<Long> userIdList);


    /**
     * 根据角色ID查询资源码
     *
     * @param roleId 角色ID
     * @return 查询结果
     */
    RolePermissionResp findRolePermissionById(Long roleId);
}
