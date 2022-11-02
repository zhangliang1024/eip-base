package com.eip.ability.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.ability.admin.domain.entity.baseinfo.OAuthClientDetails;
import com.eip.ability.admin.exception.AdminExceptionEnum;
import com.eip.ability.admin.log.SysLog;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 应用管理
 *
 * @author Levin
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
@Tag(name = "应用管理", description = "应用管理")
public class ApplicationController {


    private final ApplicationService applicationService;

    @GetMapping
    @Parameters({@Parameter(description = "应用ID", name = "clientId", in = ParameterIn.QUERY), @Parameter(description = "应用名称", name = "clientName", in = ParameterIn.QUERY),})
    @Operation(summary = "应用列表 - [Levin] - [DONE]")
    public IPage<OAuthClientDetails> query(@Parameter(description = "当前页") @RequestParam(required = false, defaultValue = "1") Integer current,
                                           @Parameter(description = "条数") @RequestParam(required = false, defaultValue = "20") Integer size, String clientId, String clientName) {
        return this.applicationService.page(new Page<>(current, size), Wraps.<OAuthClientDetails>lbQ().like(OAuthClientDetails::getClientId, clientId).like(OAuthClientDetails::getClientName,
                clientName));
    }

    @PostMapping
    @SysLog(value = "添加应用")
    @Operation(summary = "添加应用")
    public void save(@Validated @RequestBody OAuthClientDetails dto) {
        final long count = this.applicationService.count(Wraps.<OAuthClientDetails>lbQ().eq(OAuthClientDetails::getClientId, dto.getClientId()));
        AdminExceptionEnum.USER_ID_HAVED_EXIST.assertIsFalse(count > 0);
        this.applicationService.save(dto);
    }

    @PutMapping("/{id}")
    @SysLog(value = "修改应用")
    @Operation(summary = "修改应用")
    public void edit(@PathVariable String id, @Validated @RequestBody OAuthClientDetails dto) {
        this.applicationService.updateById(dto);
    }

    @PutMapping("/{id}/{status}")
    @SysLog(value = "修改应用")
    @Operation(summary = "修改应用")
    public void status(@PathVariable String id, @PathVariable Boolean status) {
        this.applicationService.updateById(OAuthClientDetails.builder().clientId(id).status(status).build());
    }

    @DeleteMapping("{id}")
    @SysLog(value = "删除应用")
    @Operation(summary = "删除应用")
    public void del(@PathVariable String id) {
        this.applicationService.removeById(id);
    }


}
