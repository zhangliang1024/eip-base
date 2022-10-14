package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.baseinfo.ApiPerm;
import com.eip.ability.admin.domain.entity.baseinfo.RoleApi;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>
 * Mapper 接口
 * 角色分配
 * 账号角色绑定
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
@TenantDS
@Repository
public interface ApiRoleMapper extends SuperMapper<RoleApi> {


}
