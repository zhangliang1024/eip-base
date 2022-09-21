package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.baseinfo.RoleRes;
import com.eip.ability.admin.domain.vo.RoleResMenuMapperResp;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * 角色的资源
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
@TenantDS
@Repository
public interface RoleResMapper extends SuperMapper<RoleRes> {


    /**
     * 根据角色ID查询角色资源菜单信息
     *
     * @param roleId 角色ID
     * @return 查询结果
     */
    List<RoleResMenuMapperResp> selectRoleResByRoleId(@Param("roleId") Long roleId);

}
