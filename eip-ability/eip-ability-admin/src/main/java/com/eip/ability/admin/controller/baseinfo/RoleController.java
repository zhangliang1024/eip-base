package com.eip.ability.admin.controller.baseinfo;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.ability.admin.domain.dto.RoleApiDTO;
import com.eip.ability.admin.domain.vo.*;
import com.eip.ability.admin.mybatis.TenantEnvironment;
import com.eip.ability.admin.domain.Result;
import com.eip.ability.admin.domain.dto.RoleDTO;
import com.eip.ability.admin.domain.dto.RoleResSaveDTO;
import com.eip.ability.admin.domain.dto.RoleUserDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.entity.baseinfo.RoleOrg;
import com.eip.ability.admin.domain.entity.baseinfo.RoleRes;
import com.eip.ability.admin.domain.enums.DataScopeType;
import com.eip.ability.admin.log.SysLog;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.*;
import com.eip.ability.admin.util.BeanUtilPlus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 *
 * @author Levin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色管理")
public class RoleController {

    private final TenantEnvironment tenantEnvironment;
    private final RoleService roleService;
    private final RoleResService roleResService;
    private final RoleOrgService roleOrgService;
    private final UserRoleService userRoleService;
    private final IApiRoleService apiRoleService;


    @GetMapping("/query_all")
    @Operation(summary = "角色列表 - [Levin] - [DONE]")
    public Result<List<Role>> query() {
        final List<Role> page = this.roleService.list();
        return Result.success(page);
    }

    @GetMapping
    @Parameters({
            @Parameter(description = "名称", name = "name", in = ParameterIn.QUERY),
    })
    @Operation(summary = "角色列表 - [Levin] - [DONE]")
    public IPage<Role> query(@Parameter(description = "当前页") @RequestParam(required = false, defaultValue = "1") Integer current,
                             @Parameter(description = "条数") @RequestParam(required = false, defaultValue = "20") Integer size,
                             String name, Boolean locked, DataScopeType scopeType) {
        return this.roleService.page(new Page<>(current, size), Wraps.<Role>lbQ()
                .eq(Role::getLocked, locked).like(Role::getName, name).eq(Role::getScopeType, scopeType));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "角色详情")
    public RoleDetailVO details(@PathVariable Long id) {
        Role role = roleService.getById(id);
        RoleDetailVO detail = BeanUtilPlus.toBean(role, RoleDetailVO.class);
        final RoleResVO authority = this.roleResService.findAuthorityIdByRoleId(id);
        detail.setAuthority(authority);
        return detail;
    }

    @PostMapping
    @SysLog(value = "添加角色")
    @Operation(summary = "添加角色")
    public void add(@Validated @RequestBody RoleDTO data) {
        roleService.saveRole(tenantEnvironment.userId(), data);
    }

    @PutMapping("/{id}")
    @SysLog(value = "编辑角色")
    @Operation(summary = "编辑角色")
    public void edit(@PathVariable Long id, @Validated @RequestBody RoleDTO data) {
        roleService.updateRole(id, tenantEnvironment.userId(), data);
    }

    @DeleteMapping("/{id}")
    @SysLog(value = "删除角色")
    @Operation(summary = "删除角色")
    public void del(@PathVariable Long id) {
        this.roleService.removeByRoleId(id);
    }

    @Operation(summary = "角色关联的用户")
    @GetMapping("/{roleId}/users")
    public Result<UserRoleResp> userByRoleId(@PathVariable Long roleId) {
        return Result.success(userRoleService.findUserByRoleId(roleId));
    }


    @GetMapping("/{role_id}/resources/permissions")
    @Operation(summary = "资源权限", description = "只能看到自身权限")
    public Result<RolePermissionResp> permission(@PathVariable("role_id") Long roleId) {
        return Result.success(this.roleService.findRolePermissionById(roleId));
    }


    @Operation(summary = "角色关联的资源")
    @GetMapping("/{roleId}/role_res")
    public List<RoleRes> resByRoleId(@PathVariable Long roleId) {
        return roleResService.list(Wraps.<RoleRes>lbQ().eq(RoleRes::getRoleId, roleId));
    }


    @Operation(summary = "角色关联的组织")
    @GetMapping("/{roleId}/role_rog")
    public List<RoleOrg> orgByRoleId(@PathVariable Long roleId) {
        return roleOrgService.list(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, roleId));
    }


    @Operation(summary = "角色分配操作资源")
    @PostMapping("/{roleId}/authority")
    public void distributionAuthority(@PathVariable Long roleId, @RequestBody RoleResSaveDTO dto) {
        this.roleResService.saveRoleAuthority(dto);

    }

    @Operation(summary = "角色分配用户")
    @PostMapping("/{roleId}/users")
    public void distributionUser(@PathVariable Long roleId, @RequestBody RoleUserDTO dto) {
        this.roleService.saveUserRole(roleId, dto.getUserIdList());
    }


    /*-----------分配接口-----------*/
    @Operation(summary = "角色关联的接口")
    @GetMapping("/{roleId}/apis")
    public Result<ApiRoleResp> apisByRoleId(@PathVariable Long roleId) {
        return Result.success(apiRoleService.findApiByRoleId(roleId));
    }


    @Operation(summary = "角色分配用户")
    @PostMapping("/{roleId}/apis")
    public void distributionApi(@PathVariable Long roleId, @RequestBody RoleApiDTO dto) {
        this.apiRoleService.saveApiRole(roleId, dto.getApiIdList());
    }
}
