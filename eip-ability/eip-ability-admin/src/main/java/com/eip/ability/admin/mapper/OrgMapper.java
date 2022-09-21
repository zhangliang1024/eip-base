package com.eip.ability.admin.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.eip.ability.admin.domain.entity.baseinfo.Org;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Levin
 */
@TenantDS
@Repository
public interface OrgMapper extends SuperMapper<Org> {


    /**
     * 根据父级ID的获取全部父级
     *
     * @param parentId parentId
     * @return 查询结果
     */
    @InterceptorIgnore(tenantLine = "true")
    String getTreePathByParentId(Long parentId);

    /**
     * 根据ID的获取子集
     *
     * @param id id
     * @return 查询结果
     */
    List<Org> findChildrenById(Long id);

}
