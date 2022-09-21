package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.baseinfo.RoleOrg;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.springframework.stereotype.Repository;

/**
 * @author Levin
 */
@TenantDS
@Repository
public interface RoleOrgMapper extends SuperMapper<RoleOrg> {

}
