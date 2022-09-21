package com.eip.ability.admin.service;

import com.eip.ability.admin.domain.entity.tenant.Tenant;
import com.eip.ability.admin.domain.entity.tenant.TenantConfig;
import com.eip.ability.admin.mybatis.supers.SuperService;

/**
 * @author Levin
 */
public interface TenantService extends SuperService<Tenant> {


    /**
     * 保存租户
     *
     * @param tenant 租户信息
     */
    void saveOrUpdateTenant(Tenant tenant);

    void tenantConfig(TenantConfig tenantConfig);

    void initSqlScript(Long id);
}
