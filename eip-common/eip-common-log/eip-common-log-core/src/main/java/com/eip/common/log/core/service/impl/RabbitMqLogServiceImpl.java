package com.eip.common.log.core.service.impl;

import com.eip.common.core.utils.JacksonUtil;
import com.eip.common.log.core.config.LogProperties;
import com.eip.common.log.core.constant.LogConstans;
import com.eip.common.core.log.LogOperationDTO;
import com.eip.common.log.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * ClassName: RabbitMqLogServiceImpl
 * Function:
 * Date: 2022年02月11 18:54:05
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
@EnableConfigurationProperties({LogProperties.class})
@ConditionalOnProperty(name = "eip.log.operation.pipeline", havingValue = LogConstans.RABBIT_MQ)
public class RabbitMqLogServiceImpl implements LogService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LogProperties properties;

    @Override
    public boolean createLog(LogOperationDTO operationDTO) throws Exception {
        String routingKey = properties.getRabbitMq().getRoutingKey();
        String message = JacksonUtil.objectToStr(operationDTO);
        log.info("[operation-log] - rabbitMq send routingKey [{}] message [{}]", routingKey, message);
        rabbitTemplate.convertAndSend(routingKey, message);
        return true;
    }
}
