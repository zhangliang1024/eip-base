package com.eip.ability.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.entity.baseinfo.User;
import com.eip.ability.admin.domain.entity.baseinfo.UserRole;
import com.eip.ability.admin.domain.entity.message.StationMessage;
import com.eip.ability.admin.domain.entity.message.StationMessagePublish;
import com.eip.ability.admin.domain.enums.ReceiverType;
import com.eip.ability.admin.domain.vo.CommonDataResp;
import com.eip.ability.admin.exception.AdminExceptionEnum;
import com.eip.ability.admin.exception.AdminRuntimeException;
import com.eip.ability.admin.mapper.*;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.StationMessagePublishService;
import com.eip.ability.admin.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class StationMessagePublishServiceImpl extends SuperServiceImpl<StationMessagePublishMapper, StationMessagePublish> implements StationMessagePublishService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final StationMessageMapper stationMessageMapper;
    private final UserRoleMapper userRoleMapper;
    // private final WebSocketManager webSocketManager;


    @Override
    public List<CommonDataResp> queryReceiverByType(ReceiverType type, String search) {
        AdminExceptionEnum.USER_RECEIVER_TYPE_NOT_EMPTY.assertNotNull(type);

        if (type == ReceiverType.USER) {
            final List<User> users = userMapper.selectList(Wraps.<User>lbQ().eq(User::getStatus, 1).and(StringUtils.isNotBlank(search),
                    wrapper -> wrapper.like(User::getNickName, search).or().like(User::getUsername, search)));
            if (CollectionUtil.isEmpty(users)) {
                return null;
            }
            return users.stream().map(user -> CommonDataResp.builder().id(user.getId()).name(user.getNickName()).build()).collect(toList());
        }
        final List<Role> roles = roleMapper.selectList(Wraps.<Role>lbQ().eq(Role::getLocked, false).like(Role::getName, search).or().like(Role::getCode, search));
        if (CollectionUtil.isEmpty(roles)) {
            return null;
        }
        return roles.stream().map(role -> CommonDataResp.builder().id(role.getId()).name(role.getName()).build()).collect(toList());
    }


    @Override
    @DSTransactional
    public void publish(Long id) {
        final StationMessagePublish messagePublish =
                Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.SESSION_MESSAGE_NOT_FOUND.getMessage()));
        final List<Long> receiver =
                Optional.of(Arrays.stream(messagePublish.getReceiver().split(",")).mapToLong(Long::parseLong).boxed().collect(toList())).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.SESSION_MESSAGE_RECEIVE_NOT_EMPTY.getMessage()));
        StationMessagePublish record = new StationMessagePublish();
        record.setId(id);
        record.setStatus(true);
        this.baseMapper.updateById(record);
        final ReceiverType type = messagePublish.getType();
        if (ReceiverType.USER.eq(type)) {
            publish(messagePublish, receiver);
        } else if (ReceiverType.ROLE.eq(type)) {
            final List<UserRole> userRoles = this.userRoleMapper.selectList(Wraps.<UserRole>lbQ().in(UserRole::getRoleId, receiver));
            if (CollectionUtil.isEmpty(userRoles)) {
                return;
            }
            publish(messagePublish, userRoles.stream().mapToLong(UserRole::getUserId).boxed().collect(Collectors.toList()));
        }
    }

    void publish(StationMessagePublish messagePublish, List<Long> userIdList) {
        for (Long userId : userIdList) {
            StationMessage message = new StationMessage();
            message.setTitle(messagePublish.getTitle());
            message.setMark(false);
            message.setContent(messagePublish.getContent());
            message.setDescription(messagePublish.getDescription());
            message.setLevel(messagePublish.getLevel());
            message.setReceiveId(userId);
            message.setCreatedTime(LocalDateTime.now());
            this.stationMessageMapper.insert(message);
            // this.webSocketManager.sendMessage(String.valueOf(userId), JSON.toJSONString(message));
        }
    }
}
