package com.eip.ability.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.eip.ability.admin.mybatis.TenantEnvironment;
import com.eip.ability.admin.domain.Entity;
import com.eip.ability.admin.domain.dto.ResourceQueryDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Resource;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.entity.baseinfo.RoleRes;
import com.eip.ability.admin.domain.enums.ResourceType;
import com.eip.ability.admin.domain.vo.VueRouter;
import com.eip.ability.admin.mapper.ResourceMapper;
import com.eip.ability.admin.mapper.RoleMapper;
import com.eip.ability.admin.mapper.RoleResMapper;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.ResourceService;
import com.eip.ability.admin.service.RoleResService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * <p>
 * 业务实现类
 * 资源
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends SuperServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final RoleMapper roleMapper;
    private final RoleResService roleResService;
    private final RoleResMapper roleResMapper;
    private final TenantEnvironment tenantEnvironment;

    @Override
    public List<VueRouter> findVisibleResource(ResourceQueryDTO resource) {
        return baseMapper.findVisibleResource(resource);
    }

    public static final String DEFAULT_PATH = "/system/development/release/tenant_%s";
    public static final String DEFAULT_COMPONENT = "/system/development/build/standard";


    @Override
    @DSTransactional
    public void addResource(Resource resource) {
        if (ResourceType.BUILD_PUBLISH.eq(resource.getType())) {
            resource.setPath(String.format(DEFAULT_PATH, tenantEnvironment.tenantId()) + "/" + resource.getModel());
            resource.setComponent(DEFAULT_COMPONENT);
        }
        if (ResourceType.MENU == resource.getType()) {
            if (!StringUtils.startsWith(resource.getPath(), "/")) {
                resource.setPath("/" + resource.getPath());
            }
        }
        final String treePath = this.baseMapper.getTreePathByParentId(resource.getParentId());
        if (StringUtils.isNotBlank(treePath)) {
            resource.setTreePath(treePath);
        }
        this.baseMapper.insert(resource);
        final List<Role> roles = this.roleMapper.selectList(Wraps.<Role>lbQ().eq(Role::getSuperRole, true)
                .eq(Role::getLocked, false));
        if (CollUtil.isEmpty(roles)) {
            return;
        }
        // 给管理员角色挂载权限
        final List<RoleRes> resList = roles.stream().map(role -> RoleRes.builder()
                .roleId(role.getId()).resId(resource.getId()).build()).collect(toList());
        roleResService.saveBatch(resList);
    }

    @Override
    public void editResourceById(Resource resource) {
        if (ResourceType.BUILD_PUBLISH.eq(resource.getType())) {
            resource.setPath(String.format(DEFAULT_PATH, tenantEnvironment.tenantId()) + "/" + resource.getModel());
            resource.setComponent(DEFAULT_COMPONENT);
        }
        this.baseMapper.updateById(resource);
    }

    @Override
    @DSTransactional
    public void delResource(Long resourceId) {
        List<Long> resourceIds = Lists.newArrayList(resourceId);
        final List<Resource> children = this.baseMapper.findChildrenById(resourceId);
        if (CollectionUtil.isNotEmpty(children)) {
            resourceIds.addAll(children.stream().map(Entity::getId).collect(toList()));
        }
        // 删除菜单和按钮
        this.baseMapper.deleteBatchIds(resourceIds);
        // 删除对应的资源权限
        this.roleResMapper.delete(Wraps.<RoleRes>lbQ().in(RoleRes::getResId, resourceIds));
    }
}
