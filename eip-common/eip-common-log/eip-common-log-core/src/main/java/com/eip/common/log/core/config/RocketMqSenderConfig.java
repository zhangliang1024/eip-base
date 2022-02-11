package com.eip.common.log.core.config;

import com.eip.common.log.core.constant.LogConstans;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * ClassName: RocketMqSenderConfig
 * Function:
 * Date: 2022年02月11 14:58:27
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({LogProperties.class})
@ConditionalOnProperty(name = "eip.log.operation.pipeline", havingValue = LogConstans.ROCKET_MQ)
public class RocketMqSenderConfig {

    private String namesrvAddr;
    private String groupName;
    private int maxMessageSize;
    private int sendMsgTimeout;
    private int retryTimeWhenSendFailed;
    private String topic;


    @Autowired
    private LogProperties properties;

    @PostConstruct
    public void rocketMqConfig() {
        this.namesrvAddr = properties.getRocketMq().getNamesrvAddr();
        this.groupName = properties.getRocketMq().getGroupName();
        this.maxMessageSize = properties.getRocketMq().getMaxMessageSize();
        this.sendMsgTimeout = properties.getRocketMq().getSendMsgTimeout();
        this.retryTimeWhenSendFailed = properties.getRocketMq().getRetryTimesWhenSendFaild();
        this.topic = properties.getRocketMq().getTopic();
        log.info("[operation-log] - rocketMq sender configuration [ namesrvAddr : {} , groupName ：{} , maxMessageSize : {} , sendMsgTimeout : {} , retryTimeWhenSendFailed : {} , topic : {} ]"
                , namesrvAddr, groupName, maxMessageSize, sendMsgTimeout, retryTimeWhenSendFailed, topic);
    }

    @Bean
    public DefaultMQProducer defaultMQProducer() throws RuntimeException {
        DefaultMQProducer producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setCreateTopicKey(this.topic);

        //发送消息最大限制 默认 0
        producer.setMaxMessageSize(this.maxMessageSize);
        //发送消息超时时间 默认 3000
        producer.setSendMsgTimeout(this.sendMsgTimeout);
        //发送失败重试次数 默认 2
        producer.setRetryTimesWhenSendFailed(this.retryTimeWhenSendFailed);
        try {
            producer.start();
            log.info("[operation-log] - rocketmq producer started ...");
        } catch (MQClientException e) {
            log.error("[operation-log] - rocketmq producer start failed", e);
            throw new RuntimeException(e);
        }
        return producer;
    }


}
