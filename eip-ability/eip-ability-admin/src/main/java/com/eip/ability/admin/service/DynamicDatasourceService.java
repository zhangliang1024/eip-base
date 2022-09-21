package com.eip.ability.admin.service;


import com.eip.ability.admin.domain.entity.tenant.DynamicDatasource;
import com.eip.ability.admin.domain.vo.TenantDynamicDatasourceVO;
import com.eip.ability.admin.mybatis.supers.SuperService;

import java.util.List;

/**
 * @author Levvin
 */
public interface DynamicDatasourceService extends SuperService<DynamicDatasource> {

    /**
     * 查询所有可用的动态数据源
     *
     * @return 查询结果
     */
    List<TenantDynamicDatasourceVO> selectTenantDynamicDatasource();

    void ping(Long id);

    void saveOrUpdateDatabase(DynamicDatasource dynamicDatasource);

    void removeDatabaseById(Long id);

    //void publishEvent(EventAction action, Long tenantId);
}
