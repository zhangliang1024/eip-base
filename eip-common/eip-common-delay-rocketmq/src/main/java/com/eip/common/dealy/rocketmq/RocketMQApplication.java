package com.eip.common.dealy.rocketmq;

import com.alibaba.fastjson.JSON;
import com.eip.common.dealy.rocketmq.producer.DelayMqProducer;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RocketMQApplication
 * Function:
 * Date: 2022年03月04 13:20:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@SpringBootApplication
public class RocketMQApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(RocketMQApplication.class,args);

        DelayMqProducer producer = new DelayMqProducer("guava-group");
        producer.setNamesrvAddr("namesrv1.bw365.net:9876;namesrv2.bw365.net:9876");
        producer.setRetryTimesWhenSendFailed(3);
        producer.start();
        Map<String, String> content = new HashMap<>();
        content.put("name", "guava");
        content.put("message", "hello word");
        Message message = new Message("guava_hello_topic", null, JSON.toJSONBytes(content));
        SendResult sendResult = producer.sendDelay(message, DateUtils.addSeconds(new Date(), 89));
        System.out.println("发送返回结果：" + JSON.toJSONString(sendResult));
        System.out.println("消息发送时间：" + String.format("%tF %<tT", new Date()));

    }

}
