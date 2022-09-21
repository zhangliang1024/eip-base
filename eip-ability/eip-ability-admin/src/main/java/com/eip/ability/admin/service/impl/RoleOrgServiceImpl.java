package com.eip.ability.admin.service.impl;

import com.eip.ability.admin.domain.entity.baseinfo.RoleOrg;
import com.eip.ability.admin.mapper.RoleOrgMapper;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.RoleOrgService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 角色组织关系
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
@Slf4j
@Service
public class RoleOrgServiceImpl extends SuperServiceImpl<RoleOrgMapper, RoleOrg> implements RoleOrgService {
    @Override
    public List<Long> listOrgByRoleId(Long id) {
        List<RoleOrg> list = super.list(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, id));
        List<Long> orgList = list.stream().mapToLong(RoleOrg::getOrgId).boxed().collect(Collectors.toList());
        return orgList;
    }
}
