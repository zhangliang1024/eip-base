package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.tenant.Tenant;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Levin
 */
@Repository
public interface TenantMapper extends SuperMapper<Tenant> {

    /**
     * 测试数据源获取数据
     *
     * @return 查询结果
     */
    @Select("select * from t_tenant")
    List<Tenant> queryDbTestTenant();
}
