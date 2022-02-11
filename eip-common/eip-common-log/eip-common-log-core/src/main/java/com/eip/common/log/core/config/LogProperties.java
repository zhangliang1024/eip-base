package com.eip.common.log.core.config;

import com.eip.common.log.core.constant.LogConstans;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: LogProperties
 * Function:
 * Date: 2022年02月11 14:49:14
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(prefix = "eip.log.operation")
public class LogProperties {

    /**
     * message pipeline : rocketmq rabbitmq stream
     */
    private String pipeline;

    private RabbitMqProperties rabbitMq;

    private RocketMqProperties rocketMq;

    private StreamProperties stream;

    @Data
    public static class RabbitMqProperties {
        private String host;
        private int port;
        private String username;
        private String password;
        private String queueName;
        private String exchangeName;
        private String routingKey;
    }

    @Data
    public static class RocketMqProperties {
        private String topic = LogConstans.ROCKET_MQ_TOPIC;
        private String tag = LogConstans.ROCKET_MQ_TAG;
        private String namesrvAddr = LogConstans.ROCKET_MQ_NAMESRV_ADDR;
        private String groupName = LogConstans.ROCKET_MQ_GROUP_NAME;
        private int maxMessageSize = LogConstans.ROCKET_MQ_MAXMESSAGE_SIZE;
        private int sendMsgTimeout = LogConstans.ROCKET_MQ_SEND_MSG_TIMEOUT;
        private int retryTimesWhenSendFaild = LogConstans.ROCKET_MQ_RETRY_TIMES_WHEN_SEND_FAILD;
    }

    @Data
    public class StreamProperties {

        /**
         * 对应消息中间件topic Rocketmq的topic, RabbtiMq的exchangeName
         */
        private String destination;
        /**
         * 默认分组
         */
        private String group;
        /**
         * 默认的binder(对应的消息中间件)
         */
        private String binder;
    }
}
