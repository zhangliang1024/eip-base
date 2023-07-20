package com.eip.common.dealy.rocketmq.listener;

import com.eip.common.dealy.rocketmq.producer.DelayMqProducer;
import com.eip.common.dealy.rocketmq.constant.GuavaRocketConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class GuavaMessageListener implements MessageListenerConcurrently {

    private DelayMqProducer delayMqProducer;

    public GuavaMessageListener(DelayMqProducer delayMqProducer) {
        this.delayMqProducer = delayMqProducer;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            for (MessageExt messageExt : msgs) {
                Map<String, String> properties = messageExt.getProperties();

                String topic = properties.get(GuavaRocketConstants.GUAVA_ORIGINAL_TOPIC);
                String times = properties.get(GuavaRocketConstants.GUAVA_TIMES);
                String tag = properties.get(GuavaRocketConstants.GUAVA_ORIGINAL_TAG);
                String keys = properties.get(GuavaRocketConstants.GUAVA_ORIGINAL_KEY);
                String uuid = properties.get(GuavaRocketConstants.GUAVA_ORIGINAL_UUID);
                if (StringUtils.isBlank(topic)) {
                    continue;
                }
                if (StringUtils.isBlank(times)) {
                    log.error("该延时消息未收到延时时间");
                    continue;
                }
                properties.remove(GuavaRocketConstants.GUAVA_TIMES);
                log.info("消息了uuId {} --topic: {}-- tags: {} #####body:{}", uuid, messageExt.getTopic(), messageExt.getTags(), new String(messageExt.getBody()));
                Message message = new Message();
                message.setTopic(topic);
                if (StringUtils.isNotBlank(tag)) {
                    message.setTags(tag);
                }
                if (StringUtils.isNotBlank(keys)) {
                    message.setKeys(keys);
                }
                if (StringUtils.isNotBlank(uuid)) {
                    message.putUserProperty(GuavaRocketConstants.GUAVA_ORIGINAL_UUID, uuid);
                }
                message.setBody(messageExt.getBody());
                delayMqProducer.sendDelay(message, new Date(Long.valueOf(times) * 1000L));
                //本次消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        } catch (Exception e) {
            log.error("消息发送失败", e);
        }
        //本次消费失败 稍后重试
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}