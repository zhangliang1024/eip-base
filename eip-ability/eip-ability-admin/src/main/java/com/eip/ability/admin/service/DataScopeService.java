package com.eip.ability.admin.service;

import com.eip.ability.admin.domain.DataScope;

/**
 * @author Levin
 */
public interface DataScopeService {

    /**
     * 根据用户编号获取数据权限
     *
     * @param userId 用户ID
     * @return 查询结果
     */
    DataScope getDataScopeById(Long userId);
}
