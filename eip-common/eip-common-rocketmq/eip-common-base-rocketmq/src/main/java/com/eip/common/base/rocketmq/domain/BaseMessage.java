package com.eip.common.base.rocketmq.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: BaseMessage
 * Function: 所有消息都需要继承此类
 * Date: 2023年07月20 15:14:10
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public abstract class BaseMessage {
    /**
     * 业务键，用于RocketMQ控制台查看消费情况
     */
    protected String key;
    /**
     * 发送消息来源，用于排查问题
     */
    protected String source = "";
    /**
     * 发送时间
     */
    protected LocalDateTime sendTime = LocalDateTime.now();
    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    protected Integer retryTimes = 0;
}