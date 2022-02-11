package com.eip.common.log.core.config;

import com.eip.common.log.core.constant.LogConstans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * ClassName: RabbitMqSenderConfig
 * Function:
 * Date: 2022年02月11 18:39:40
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "eip.log.operation.pipeline", havingValue = LogConstans.RABBIT_MQ)
@EnableConfigurationProperties({LogProperties.class})
public class RabbitMqSenderConfig {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String queue;
    private String exchange;
    private String routingKey;

    @Autowired
    private LogProperties properties;

    @PostConstruct
    public void rabbitMqConfig() {
        this.host = properties.getRabbitMq().getHost();
        this.port = properties.getRabbitMq().getPort();
        this.username = properties.getRabbitMq().getUsername();
        this.password = properties.getRabbitMq().getPassword();
        this.queue = properties.getRabbitMq().getQueueName();
        this.exchange = properties.getRabbitMq().getExchangeName();
        this.routingKey = properties.getRabbitMq().getRoutingKey();
        log.info("[operation-log] - rabbitMq sender configuration [ host : {} , port : {} , exchange : {} , queue : {} , routingKey : {} ]",
                host, port, exchange, queue, routingKey);
    }

    @Bean
    Binding exchangeBinding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host, port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rubeExchangeTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setExchange(exchange);
        template.setRoutingKey(routingKey);
        template.setConnectionFactory(factory);
        return template;
    }
}
