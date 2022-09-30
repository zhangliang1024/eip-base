package com.eip.ability.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.eip.ability.admin.domain.entity.baseinfo.ApiPerm;
import com.eip.ability.admin.domain.entity.baseinfo.RoleApi;
import com.eip.ability.admin.domain.vo.ApiRoleResp;
import com.eip.ability.admin.mapper.ApiPermMapper;
import com.eip.ability.admin.mapper.ApiRoleMapper;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.IApiRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: ApiRoleServiceImpl
 * Function:
 * Date: 2022年09月30 16:28:02
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRoleServiceImpl extends SuperServiceImpl<ApiRoleMapper, RoleApi> implements IApiRoleService {

    private final ApiPermMapper apiPermMapper;
    private final ApiRoleMapper apiRoleMapper;

    @Override
    public ApiRoleResp findApiByRoleId(Long roleId) {
        final List<Long> apiIdList = super.list(Wraps.<RoleApi>lbQ().eq(RoleApi::getRoleId, roleId)).stream().map(RoleApi::getApiId).distinct().collect(Collectors.toList());
        final List<ApiPerm> apiPerms = apiPermMapper.selectList(Wraps.lbQ());
        if (CollectionUtil.isEmpty(apiPerms)) {
            return null;
        }
        final List<ApiRoleResp.ApiRoleDetail> apiRoleDetails = apiPerms.stream().map(apiPerm -> ApiRoleResp.ApiRoleDetail.builder()
                .id(apiPerm.getId())
                .apiName(apiPerm.getApiName())
                .apiMethod(apiPerm.getApiMethod())
                .apiPath(apiPerm.getApiPath())
                .build())
                .collect(Collectors.toList());
        return ApiRoleResp.builder().apiRoleDetails(apiRoleDetails).originTargetKeys(apiIdList).build();
    }

    @Override
    public void saveApiRole(Long roleId, List<Long> apiIdList) {
        this.apiRoleMapper.delete(Wraps.<RoleApi>lbQ().eq(RoleApi::getRoleId, roleId));
        final List<RoleApi> roleApiList = apiIdList.stream().map(userId -> RoleApi.builder()
                .roleId(roleId).apiId(userId).build())
                .collect(Collectors.toList());
        for (RoleApi roleApi : roleApiList) {
            this.apiRoleMapper.insert(roleApi);
        }
    }
}
