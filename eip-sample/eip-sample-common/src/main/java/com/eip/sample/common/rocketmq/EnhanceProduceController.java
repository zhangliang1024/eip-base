package com.eip.sample.common.rocketmq;

import com.eip.common.base.rocketmq.domain.BaseMessage;
import com.eip.common.base.rocketmq.template.RocketMQEnhanceTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("enhance")
@Slf4j
public class EnhanceProduceController {
    // 注入增强后的模板，可以自动实现环境隔离，日志记录
    @Setter(onMethod_ = @Autowired)
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    private static final String topic = "rocket_enhance";
    private static final String tag = "member";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /**
     * 发送实体消息
     */
    @GetMapping("/member")
    public SendResult member() {
        String key = UUID.randomUUID().toString();
        MemberMessage message = new MemberMessage();
        // 设置业务key
        message.setKey(key);
        // 设置消息来源，便于查询
        message.setSource("MEMBER");
        // 业务消息内容
        message.setUserName("Java日知录");
        message.setBirthday(LocalDate.now());
        return rocketMQEnhanceTemplate.send(topic, tag, message);
    }
}