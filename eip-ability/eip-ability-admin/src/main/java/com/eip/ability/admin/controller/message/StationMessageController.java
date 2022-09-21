package com.eip.ability.admin.controller.message;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.ability.admin.mybatis.TenantEnvironment;
import com.eip.ability.admin.domain.PageRequest;
import com.eip.ability.admin.domain.Result;
import com.eip.ability.admin.domain.entity.message.StationMessage;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.service.StationMessageService;
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
@Validated
@RestController
@Tag(name = "站内消息")
@RequiredArgsConstructor
@TenantDS
public class StationMessageController {

    private final TenantEnvironment tenantEnvironment;
    private final StationMessageService stationMessageService;

    @GetMapping("/station_messages/my")
    public Result<Page<StationMessage>> myMessage(String title, String level, Boolean mark, PageRequest request) {
        final Page<StationMessage> page = stationMessageService.page(request.buildPage(), Wraps.<StationMessage>lbQ()
                .like(StationMessage::getTitle, title).eq(StationMessage::getLevel, level)
                .eq(StationMessage::getMark, mark).eq(StationMessage::getReceiveId, tenantEnvironment.userId()));
        return Result.success(page);
    }

    @GetMapping("/users/{user_id}/station_messages")
    public Result<List<StationMessage>> load(@PathVariable("user_id") Long userId) {
        final List<StationMessage> messages = this.stationMessageService.list(Wraps.<StationMessage>lbQ()
                .eq(StationMessage::getMark, false)
                .eq(StationMessage::getReceiveId, userId));
        return Result.success(messages);
    }

    @PatchMapping("/station_messages/{message_id}/mark")
    public void mark(@PathVariable("message_id") Long messageId) {
        StationMessage message = new StationMessage();
        message.setId(messageId);
        message.setMark(true);
        this.stationMessageService.updateById(message);
        
    }


    @DeleteMapping("/station_messages/{message_id}")
    public void del(@PathVariable("message_id") Long messageId) {
        this.stationMessageService.removeById(messageId);
        
    }

    @DeleteMapping("/station_messages")
    public void batchDel(@RequestBody List<Long> ids) {
        this.stationMessageService.removeByIds(ids);
        
    }


}
