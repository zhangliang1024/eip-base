package com.eip.ability.admin.service;


import com.eip.ability.admin.domain.entity.message.StationMessagePublish;
import com.eip.ability.admin.domain.enums.ReceiverType;
import com.eip.ability.admin.domain.vo.CommonDataResp;
import com.eip.ability.admin.mybatis.supers.SuperService;

import java.util.List;

/**
 * @author levin
 */
public interface StationMessagePublishService extends SuperService<StationMessagePublish> {

    /**
     * 根据类型和条件查询
     *
     * @param type   类型
     * @param search 条件
     * @return 查询结果
     */
    List<CommonDataResp> queryReceiverByType(ReceiverType type, String search);

    /**
     * 发布消息
     *
     * @param id id
     */
    void publish(Long id);
}
