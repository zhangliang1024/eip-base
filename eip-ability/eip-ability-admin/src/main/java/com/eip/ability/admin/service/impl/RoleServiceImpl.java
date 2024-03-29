package com.eip.ability.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.eip.ability.admin.domain.DataScope;
import com.eip.ability.admin.domain.dto.ResourceQueryDTO;
import com.eip.ability.admin.domain.dto.RoleDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.entity.baseinfo.RoleOrg;
import com.eip.ability.admin.domain.entity.baseinfo.RoleRes;
import com.eip.ability.admin.domain.entity.baseinfo.UserRole;
import com.eip.ability.admin.domain.vo.RolePermissionResp;
import com.eip.ability.admin.domain.vo.VueRouter;
import com.eip.ability.admin.exception.AdminExceptionEnum;
import com.eip.ability.admin.exception.AdminRuntimeException;
import com.eip.ability.admin.mapper.*;
import com.eip.ability.admin.mybatis.TenantEnvironment;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.RoleService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * <p>
 * 业务实现类
 * 角色
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends SuperServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleResMapper roleResMapper;
    private final RoleOrgMapper roleOrgMapper;
    private final UserRoleMapper userRoleMapper;
    private final ResourceMapper resourceMapper;
    private final TenantEnvironment tenantEnvironment;

    @Override
    public List<Role> list(DataScope scope) {
        return baseMapper.list(scope);
    }

    @Override
    @DSTransactional
    public void removeByRoleId(Long roleId) {
        final Role role = Optional.ofNullable(baseMapper.selectById(roleId)).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.SYSTEM_ROLE_NOT_FOUND.getMessage()));
        AdminExceptionEnum.SYSTEM_ROLE_DONOT_DELETE.assertIsFalse(role.getReadonly());
        AdminExceptionEnum.SUPER_ROLE_DONOT_DELETE.assertIsFalse(role.getSuperRole());

        baseMapper.deleteById(roleId);
        roleOrgMapper.delete(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, roleId));
        roleResMapper.delete(Wraps.<RoleRes>lbQ().eq(RoleRes::getRoleId, roleId));
        userRoleMapper.delete(Wraps.<UserRole>lbQ().eq(UserRole::getRoleId, roleId));
    }


    @Override
    public void saveRole(Long userId, RoleDTO data) {
        Role role = BeanUtil.toBean(data, Role.class);
        role.setReadonly(false);
        super.save(role);
        saveRoleOrg(role, data.getOrgList());
    }

    @Override
    public void updateRole(Long roleId, Long userId, RoleDTO data) {
        Role role = BeanUtil.toBean(data, Role.class);
        AdminExceptionEnum.SYSTEM_ROLE_DONOT_EDIT.assertIsFalse(role.getReadonly() != null && role.getReadonly());
        AdminExceptionEnum.SUPER_ROLE_DONOT_EDIT.assertIsFalse(role.getSuperRole() != null && role.getSuperRole());

        role.setId(roleId);
        baseMapper.updateById(role);

        roleOrgMapper.delete(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, roleId));
        saveRoleOrg(role, data.getOrgList());
    }

    @Override
    @DSTransactional
    public void saveUserRole(Long roleId, List<Long> userIdList) {
        this.userRoleMapper.delete(Wraps.<UserRole>lbQ().eq(UserRole::getRoleId, roleId));
        final List<UserRole> userRoles = userIdList.stream().map(userId -> UserRole.builder().roleId(roleId).userId(userId).build()).collect(Collectors.toList());
        for (UserRole role : userRoles) {
            this.userRoleMapper.insert(role);
        }
    }

    private void saveRoleOrg(Role role, List<Long> orgList) {
        // 根据 数据范围类型 和 勾选的组织ID， 重新计算全量的组织ID
        if (CollectionUtil.isNotEmpty(orgList)) {
            List<RoleOrg> list = orgList.stream().map((orgId) -> RoleOrg.builder().orgId(orgId).roleId(role.getId()).build()).collect(Collectors.toList());
            for (RoleOrg roleOrg : list) {
                roleOrgMapper.insert(roleOrg);
            }
        }
    }

    @Override
    public RolePermissionResp findRolePermissionById(Long roleId) {
        final List<VueRouter> buttons = resourceMapper.findVisibleResource(ResourceQueryDTO.builder().userId(tenantEnvironment.userId()).build());
        final List<Long> roleRes =
                Optional.of(this.roleResMapper.selectList(Wraps.<RoleRes>lbQ().eq(RoleRes::getRoleId, roleId))).orElse(Lists.newArrayList()).stream().map(RoleRes::getResId).collect(toList());
        return RolePermissionResp.builder().resIdList(roleRes).buttons(buttons).build();
    }
}
