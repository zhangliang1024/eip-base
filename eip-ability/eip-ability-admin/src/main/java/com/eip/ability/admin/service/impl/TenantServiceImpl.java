package com.eip.ability.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.entity.baseinfo.User;
import com.eip.ability.admin.domain.entity.baseinfo.UserRole;
import com.eip.ability.admin.domain.entity.common.AreaEntity;
import com.eip.ability.admin.domain.entity.tenant.Tenant;
import com.eip.ability.admin.domain.entity.tenant.TenantConfig;
import com.eip.ability.admin.exception.AdminExceptionEnum;
import com.eip.ability.admin.exception.AdminRuntimeException;
import com.eip.ability.admin.mapper.*;
import com.eip.ability.admin.mybatis.properties.DatabaseProperties;
import com.eip.ability.admin.mybatis.properties.MultiTenantType;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.DynamicDatasourceService;
import com.eip.ability.admin.service.TenantService;
import com.eip.ability.admin.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends SuperServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final AreaMapper areaMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final TenantConfigMapper tenantConfigMapper;
    private final DynamicDatasourceService dynamicDatasourceService;
    private final DatabaseProperties properties;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveOrUpdateTenant(Tenant tenant) {
        tenant.setProvinceName(getNameById(tenant.getProvinceId()));
        tenant.setCityName(getNameById(tenant.getCityId()));
        tenant.setDistrictName(getNameById(tenant.getDistrictId()));
        if (tenant.getId() != null) {
            baseMapper.updateById(tenant);
            return;
        }
        baseMapper.insert(tenant);
    }

    private String getNameById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        final AreaEntity areaEntity = areaMapper.selectById(id);
        if (Objects.isNull(areaEntity)) {
            return null;
        }
        return areaEntity.getName();
    }

    @Override
    public void tenantConfig(TenantConfig tenantConfig) {
        final Tenant tenant =
                Optional.ofNullable(this.baseMapper.selectById(tenantConfig.getTenantId())).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.TENANT_NOT_FOUNT.getMessage()));
        AdminExceptionEnum.TENANT_HAS_BEEN_DISABLED.assertIsFalse(tenant.getLocked());
        AdminExceptionEnum.SUPER_TENANT_DONOT_EDIT.assertIsFalse(StringUtils.equals(tenant.getCode(), properties.getMultiTenant().getSuperTenantCode()));

        if (tenantConfig.getId() == null) {
            tenantConfigMapper.delete(Wraps.<TenantConfig>lbQ().eq(TenantConfig::getTenantId, tenantConfig.getTenantId()));
            tenantConfigMapper.insert(tenantConfig);
        } else {
            tenantConfigMapper.updateById(tenantConfig);
        }
        // 先创建
        // dynamicDatasourceService.publishEvent(EventAction.INIT, tenantConfig.getTenantId());
    }

    @Override
    @DSTransactional
    public void initSqlScript(Long id) {
        final Tenant tenant = Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.TENANT_NOT_FOUNT.getMessage()));

        AdminExceptionEnum.TENANT_HAS_BEEN_DISABLED.assertIsFalse(tenant.getLocked());
        final DatabaseProperties.MultiTenant multiTenant = properties.getMultiTenant();
        AdminExceptionEnum.SUPER_TENANT_DONOT_EDIT.assertIsFalse(StringUtils.equals(tenant.getCode(), multiTenant.getSuperTenantCode()));

        if (multiTenant.getType() == MultiTenantType.COLUMN) {
            final Role role =
                    Optional.ofNullable(roleMapper.selectOne(Wraps.<Role>lbQ().eq(Role::getCode, "TENANT-ADMIN"))).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.SYSTEM_TENANT_ROLE_NOT_FOUNT.getMessage()));
            final List<User> users = this.userMapper.selectByTenantId(tenant.getId());
            if (CollUtil.isNotEmpty(users)) {
                final List<Long> userIdList = users.stream().map(User::getId).distinct().collect(Collectors.toList());
                log.warn("开始清除租户 - {} 的系统数据,危险动作", tenant.getName());
                this.userRoleMapper.delete(Wraps.<UserRole>lbQ().in(UserRole::getUserId, userIdList));
                this.userMapper.deleteByTenantId(tenant.getId());
                this.roleMapper.deleteByTenantId(tenant.getId());
                log.warn("开始初始化租户 - {} 的系统数据,危险动作", tenant.getName());
            }
            User record = new User();
            record.setUsername("admin");
            record.setPassword(passwordEncoder.encode("123456"));
            record.setTenantId(id);
            record.setNickName(tenant.getContactPerson());
            record.setMobile(tenant.getContactPhone());
            record.setStatus(true);
            this.userMapper.insert(record);
            this.userRoleMapper.insert(UserRole.builder().userId(record.getId()).roleId(role.getId()).build());

        } else if (multiTenant.getType() == MultiTenantType.DATASOURCE) {
            // TenantDynamicDataSourceProcess tenantDynamicDataSourceProcess = SpringUtil.getBean(TenantDynamicDataSourceProcess.class);
            // tenantDynamicDataSourceProcess.initSqlScript(tenant.getId(), tenant.getCode());
        }
    }
}
