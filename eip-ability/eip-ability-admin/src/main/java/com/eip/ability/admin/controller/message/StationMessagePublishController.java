package com.eip.ability.admin.controller.message;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.PageRequest;
import com.eip.ability.admin.domain.Result;
import com.eip.ability.admin.domain.dto.StationMessagePublishReq;
import com.eip.ability.admin.domain.entity.message.StationMessagePublish;
import com.eip.ability.admin.domain.enums.ReceiverType;
import com.eip.ability.admin.domain.vo.CommonDataResp;
import com.eip.ability.admin.domain.vo.StationMessagePublishResp;
import com.eip.ability.admin.log.SysLog;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.service.StationMessagePublishService;
import com.eip.ability.admin.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.eip.ability.admin.domain.converts.StationMessagePublishConverts.STATION_MESSAGE_PUBLISH_2_VO_CONVERTS;


/**
 * @author Levin
 */
@TenantDS
@RequestMapping("station_messages_publish")
@RestController
@RequiredArgsConstructor
@Tag(name = "站内消息")
public class StationMessagePublishController {

    private final StationMessagePublishService stationMessagePublishService;

    @GetMapping
    public IPage<StationMessagePublishResp> publishList(PageRequest request, String title, Integer level, Integer type) {
        return stationMessagePublishService.page(request.buildPage(), Wraps.<StationMessagePublish>lbQ()
                .eq(StationMessagePublish::getTitle, title).eq(StationMessagePublish::getLevel, level)
                .eq(StationMessagePublish::getType, ReceiverType.of(type))).convert(STATION_MESSAGE_PUBLISH_2_VO_CONVERTS::convert);
    }


    @GetMapping("/{type}/receivers")
    public Result<List<CommonDataResp>> query(@PathVariable ReceiverType type, String search) {
        return Result.success(this.stationMessagePublishService.queryReceiverByType(type, search));
    }

    @PostMapping
    @SysLog(value = "添加发布消息")
    @Operation(summary = "添加发布消息")
    public void add(@Validated @RequestBody StationMessagePublishReq req) {
        final StationMessagePublish bean = BeanUtil.toBean(req, StationMessagePublish.class);
        bean.setReceiver(StringUtils.join(req.getReceiver(), ","));
        stationMessagePublishService.save(bean);
    }

    @PutMapping("/{id}")
    @SysLog(value = "编辑发布消息")
    @Operation(summary = "编辑发布消息")
    public void edit(@PathVariable Long id, @Validated @RequestBody StationMessagePublishReq req) {
        final StationMessagePublish bean = BeanUtil.toBean(req, StationMessagePublish.class);
        bean.setReceiver(StringUtils.join(req.getReceiver(), ","));
        bean.setId(id);
        stationMessagePublishService.updateById(bean);
    }

    @DeleteMapping("/{id}")
    @SysLog(value = "删除发布消息")
    @Operation(summary = "删除发布消息")
    public void del(@PathVariable Long id) {
        stationMessagePublishService.removeById(id);
    }

    @PatchMapping("/{id}/publish")
    @SysLog(value = "发布消息通知")
    @Operation(summary = "发布消息通知")
    public void publish(@PathVariable Long id) {
        stationMessagePublishService.publish(id);
    }


}
