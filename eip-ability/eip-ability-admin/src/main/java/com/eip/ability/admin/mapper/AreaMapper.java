package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.common.AreaEntity;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Levin
 */
@TenantDS
@Repository
public interface AreaMapper extends SuperMapper<AreaEntity> {

    /**
     * 根据 parentId 查询数据集
     *
     * @param parentId parentId
     * @return 查询结果
     */
    List<AreaEntity> listArea(@Param("parentId") Integer parentId);
}
