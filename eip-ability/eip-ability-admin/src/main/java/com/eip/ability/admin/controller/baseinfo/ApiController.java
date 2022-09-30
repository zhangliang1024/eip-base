package com.eip.ability.admin.controller.baseinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.Result;
import com.eip.ability.admin.domain.dto.ApiPermDTO;
import com.eip.ability.admin.domain.entity.baseinfo.ApiPerm;
import com.eip.ability.admin.domain.enums.DataScopeType;
import com.eip.ability.admin.domain.vo.UserRoleResp;
import com.eip.ability.admin.log.SysLog;
import com.eip.ability.admin.service.IApiPermService;
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
 * ClassName: ApiController
 * Function:
 * Date: 2022年09月30 13:26:35
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "接口管理", description = "接口管理")
public class ApiController {

    private final IApiPermService apiPermService;

    @GetMapping
    @Parameters({
            @Parameter(description = "接口名称", name = "apiName", in = ParameterIn.QUERY),
            @Parameter(description = "接口方法", name = "apiMethod", in = ParameterIn.QUERY),
            @Parameter(description = "接口地址", name = "apiPath", in = ParameterIn.QUERY),
            @Parameter(description = "状态", name = "locked", in = ParameterIn.QUERY),
            @Parameter(description = "权限范围", name = "scopeType", in = ParameterIn.QUERY),
    })
    @Operation(summary = "接口列表")
    public IPage<ApiPerm> query(@Parameter(description = "当前页") @RequestParam(required = false, defaultValue = "1") Integer current,
                                @Parameter(description = "条数") @RequestParam(required = false, defaultValue = "20") Integer size,
                                String apiName,String apiMethod,String apiPath,
                                Boolean locked, String scopeType) {
        return this.apiPermService.queryList(current, size, apiName, apiMethod, apiPath, locked, scopeType);
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "接口详情")
    public ApiPerm details(@PathVariable Long id) {
        ApiPerm apiPerm = apiPermService.getById(id);
        return apiPerm;
    }

    @PostMapping
    @SysLog(value = "添加接口")
    @Operation(summary = "添加接口")
    public void add(@Validated @RequestBody ApiPermDTO data) {
        apiPermService.addApi(data);
    }

    @PutMapping("/{id}")
    @SysLog(value = "编辑接口")
    @Operation(summary = "编辑接口")
    public void edit(@PathVariable Long id, @Validated @RequestBody ApiPermDTO data) {
        apiPermService.updateApi(id, data);
    }

    @DeleteMapping("/{id}")
    @SysLog(value = "删除接口")
    @Operation(summary = "删除接口")
    public void del(@PathVariable Long id) {
        this.apiPermService.removeByApiId(id);
    }

}
