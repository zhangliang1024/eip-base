package com.eip.common.log.core.service.impl;

import com.eip.common.core.utils.JacksonUtil;
import com.eip.common.log.core.config.LogProperties;
import com.eip.common.log.core.constant.LogConstans;
import com.eip.common.core.log.LogOperationDTO;
import com.eip.common.log.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * ClassName: RocketMqLogServiceImpl
 * Function:
 * Date: 2022年02月11 18:59:35
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
@EnableConfigurationProperties({LogProperties.class})
@ConditionalOnProperty(name = "eip.log.operation.pipeline", havingValue = LogConstans.ROCKET_MQ)
public class RocketMqLogServiceImpl implements LogService {

    @Autowired
    private LogProperties properties;

    @Autowired
    private DefaultMQProducer producer;

    @Override
    public boolean createLog(LogOperationDTO operationDTO) throws Exception {
        String topic = properties.getRocketMq().getTopic();
        String tag = properties.getRocketMq().getTag();
        log.info("[operation-log] - rocketMq send : topic [{}] tag [{}] LogDTO [{}]", topic, tag, operationDTO);
        Message message = new Message(topic, tag, (JacksonUtil.objectToStr(operationDTO)).getBytes(LogConstans.DEFAULT_CHARSET));
        SendResult sendResult = producer.send(message);
        log.info("[operation-log] - rocketMq sendResult : [{}]", sendResult);
        return true;
    }
}
