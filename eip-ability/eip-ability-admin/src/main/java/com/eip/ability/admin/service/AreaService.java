package com.eip.ability.admin.service;

import com.eip.ability.admin.domain.entity.common.AreaEntity;
import com.eip.ability.admin.mybatis.supers.SuperService;

import java.util.List;

/**
 * @author Levin
 */
public interface AreaService extends SuperService<AreaEntity> {

    /**
     * 根据 parentId 查询数据集
     *
     * @param parentId parentId
     * @return 查询结果
     */
    List<AreaEntity> listArea(Integer parentId);

    /**
     * 保存或者修改地区
     *
     * @param area area
     */
    void saveOrUpdateArea(AreaEntity area);

}
