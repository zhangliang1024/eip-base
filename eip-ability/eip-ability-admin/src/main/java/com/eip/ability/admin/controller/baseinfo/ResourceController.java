package com.eip.ability.admin.controller.baseinfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.ability.admin.mybatis.TenantEnvironment;
import com.eip.ability.admin.domain.Result;
import com.eip.ability.admin.domain.dto.ResourceQueryDTO;
import com.eip.ability.admin.domain.dto.ResourceSaveDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Resource;
import com.eip.ability.admin.domain.enums.ResourceType;
import com.eip.ability.admin.domain.vo.VueRouter;
import com.eip.ability.admin.log.SysLog;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.mybatis.wraps.query.LbqWrapper;
import com.eip.ability.admin.service.ResourceService;
import com.eip.ability.admin.util.BeanUtilPlus;
import com.google.common.collect.Lists;
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
import java.util.Optional;

import static com.eip.ability.admin.domain.converts.MenuConverts.VUE_ROUTER_2_TREE_NODE_CONVERTS;
import static java.util.stream.Collectors.toList;

/**
 * 菜单资源
 *
 * @author Levin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
@Tag(name = "菜单资源", description = "菜单资源")
public class ResourceController {

    private final ResourceService resourceService;
    private final TenantEnvironment tenantEnvironment;

    @GetMapping("/router")
    @Operation(summary = "菜单路由", description = "只能看到自身权限")
    public Result<List<Tree<Long>>> router(@RequestParam(required = false, defaultValue = "false") Boolean all) {
        List<VueRouter> routers = resourceService.findVisibleResource(ResourceQueryDTO.builder().userId(tenantEnvironment.userId()).build());
        List<TreeNode<Long>> list = routers.stream()
                .filter(router -> all || (router.getType() != null && router.getType() == 1 || router.getType() == 5))
                .map(VUE_ROUTER_2_TREE_NODE_CONVERTS::convert).collect(toList());
        return Result.success(TreeUtil.build(list, 0L));
    }

    @GetMapping("/permissions")
    @Operation(summary = "资源码", description = "只能看到自身资源码")
    public Result<List<String>> permissions() {
        List<VueRouter> routers = Optional.ofNullable(resourceService.findVisibleResource(ResourceQueryDTO.builder()
                .userId(tenantEnvironment.userId()).build())).orElseGet(Lists::newArrayList);
        return Result.success(routers.stream().map(VueRouter::getPermission).collect(toList()));
    }

    @GetMapping
    @Parameters({
            @Parameter(description = "父ID", name = "parentId", in = ParameterIn.QUERY),
            @Parameter(description = "资源类型", name = "type", in = ParameterIn.QUERY),
            @Parameter(description = "名称", name = "name", in = ParameterIn.QUERY),
    })
    @Operation(summary = "资源列表 - [Levin] - [DONE]")
    public IPage<Resource> query(@Parameter(description = "当前页") @RequestParam(required = false, defaultValue = "1") Integer current,
                                         @Parameter(description = "条数") @RequestParam(required = false, defaultValue = "20") Integer size,
                                         Long parentId, Integer type) {
        final LbqWrapper<Resource> wrapper = Wraps.<Resource>lbQ().eq(Resource::getParentId, parentId).eq(Resource::getType, ResourceType.BUTTON);
        return resourceService.page(new Page<>(current, size), wrapper);
    }

    @PostMapping
    @SysLog(value = "添加资源")
    @Operation(summary = "添加资源")
    public void save(@Validated @RequestBody ResourceSaveDTO data) {
        Resource resource = BeanUtil.toBean(data, Resource.class);
        resourceService.addResource(resource);
    }


    @DeleteMapping("/{id}")
    @SysLog(value = "删除资源")
    @Operation(summary = "删除资源")
    public void del(@PathVariable Long id) {
        this.resourceService.delResource(id);
    }

    @PutMapping("/{id}")
    @SysLog(value = "修改资源")
    @Operation(summary = "修改资源")
    public void edit(@PathVariable Long id, @Validated @RequestBody ResourceSaveDTO data) {
        resourceService.editResourceById(BeanUtilPlus.toBean(id, data, Resource.class));
    }


}
