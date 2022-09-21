package com.eip.ability.admin.controller.baseinfo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.dto.StationPageDTO;
import com.eip.ability.admin.domain.dto.StationSaveDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Station;
import com.eip.ability.admin.log.SysLog;
import com.eip.ability.admin.service.StationService;
import com.eip.ability.admin.util.BeanUtilPlus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 岗位管理
 *
 * @author Levin
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stations")
@Tag(name = "岗位管理", description = "岗位管理")
public class StationController {

    private final StationService stationService;

    @GetMapping
    @Operation(summary = "岗位列表 - [Levin] - [DONE]")
    public IPage<Station> query(StationPageDTO params) {
        return stationService.findStationPage(params, params);
    }

    @PostMapping
    @SysLog(value = "添加岗位")
    @Operation(summary = "添加岗位")
    public void add(@Validated @RequestBody StationSaveDTO dto) {
        stationService.save(BeanUtil.toBean(dto, Station.class));
    }

    @PutMapping("/{id}")
    @SysLog(value = "编辑岗位")
    @Operation(summary = "编辑岗位")
    public void edit(@PathVariable Long id, @Validated @RequestBody StationSaveDTO dto) {
        stationService.updateById(BeanUtilPlus.toBean(id, dto, Station.class));
    }

    @DeleteMapping("/{id}")
    @SysLog(value = "删除岗位")
    @Operation(summary = "删除岗位")
    public void del(@PathVariable Long id) {
        stationService.removeById(id);
    }

}
