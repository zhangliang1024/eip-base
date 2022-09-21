package com.eip.ability.admin.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.eip.ability.admin.domain.DataScope;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Levin
 */
@TenantDS
@Repository
public interface RoleMapper extends SuperMapper<Role> {


    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 查询结果
     */
    List<Role> findRoleByUserId(Long userId);

    /**
     * 根据范围查询角色
     *
     * @param scope scope
     * @return 查询结果
     */
    List<Role> list(DataScope scope);

    @InterceptorIgnore(tenantLine = "true")
    @Delete("delete from sys_role where tenant_id = #{tenantId}")
    void deleteByTenantId(@Param("tenantId") Long tenantId);
}
