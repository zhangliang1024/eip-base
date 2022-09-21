package com.eip.ability.admin.controller.tenant;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.ability.admin.domain.PageRequest;
import com.eip.ability.admin.domain.Result;
import com.eip.ability.admin.domain.dto.DynamicDatasourceReq;
import com.eip.ability.admin.domain.entity.tenant.DynamicDatasource;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.DynamicDatasourceService;
import com.eip.ability.admin.util.BeanUtilPlus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Levin
 */
@Slf4j
@RestController
@RequestMapping("/databases")
@RequiredArgsConstructor
@Tag(name = "数据源管理", description = "数据源管理")
@Validated
public class DynamicDatasourceController {

    private final DynamicDatasourceService dynamicDatasourceService;

    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping
    public Result<Page<DynamicDatasource>> page(PageRequest pageRequest, String database) {
        final Page<DynamicDatasource> page = dynamicDatasourceService.page(pageRequest.buildPage(),
                Wraps.<DynamicDatasource>lbQ().eq(DynamicDatasource::getDatabase, database));
        return Result.success(page);
    }

    @Operation(summary = "查询可用", description = "查询可用数据源")
    @GetMapping("/active")
    public Result<List<DynamicDatasource>> queryActive() {
        return Result.success(this.dynamicDatasourceService.list(Wraps.<DynamicDatasource>lbQ().eq(DynamicDatasource::getLocked, false)));
    }

    @Operation(summary = "Ping数据库")
    @GetMapping("/{id}/ping")
    public void ping(@PathVariable Long id) {
        this.dynamicDatasourceService.ping(id);
        
    }

    @Operation(summary = "添加数据源")
    @PostMapping
    public void add(@Validated @RequestBody DynamicDatasourceReq req) {
        dynamicDatasourceService.saveOrUpdateDatabase(BeanUtil.toBean(req, DynamicDatasource.class));
        
    }

    @Operation(summary = "编辑数据源")
    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, @Validated @RequestBody DynamicDatasourceReq req) {
        dynamicDatasourceService.saveOrUpdateDatabase(BeanUtilPlus.toBean(id, req, DynamicDatasource.class));
        
    }

    @Operation(summary = "删除数据源")
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        dynamicDatasourceService.removeDatabaseById(id);
        
    }
}
