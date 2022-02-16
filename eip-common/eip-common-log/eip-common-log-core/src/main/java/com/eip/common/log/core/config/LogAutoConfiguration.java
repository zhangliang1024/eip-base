package com.eip.common.log.core.config;

import com.eip.common.log.core.aspect.LogOperationLogAspect;
import com.eip.common.log.core.function.LogFunctionRegistrar;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;

/**
 * ClassName: LogAutoConfiguration
 * Function:
 * Date: 2022年02月11 14:31:50
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@ComponentScan("com.eip.common.log.core")
@Import({RocketMqSenderConfig.class,RabbitMqSenderConfig.class})
public class LogAutoConfiguration {

    @Autowired
    private OkHttpClient okHttpClient;

    @Bean
    LogOperationLogAspect logOperationLogAspect() {
        return new LogOperationLogAspect();
    }

    @Bean
    LogFunctionRegistrar LogFunctionRegistrar() {
        return new LogFunctionRegistrar();
    }

    //TODO 测试阶段 不接入loadBalanced负载
    @Bean("logRestTemplate")
    //@LoadBalanced
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory factory = httpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

    public ClientHttpRequestFactory httpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory(okHttpClient);
    }

    @Bean
    public ListeningExecutorService listeningExecutorService() {
        return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(50));
    }
}
