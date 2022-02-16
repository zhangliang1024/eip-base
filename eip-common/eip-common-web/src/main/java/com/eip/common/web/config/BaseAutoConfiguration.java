package com.eip.common.web.config;

import com.eip.common.web.advice.GlobalResponseAdvice;
import com.eip.common.web.apiversion.config.ApiVersionWebConfiguration;
import com.eip.common.web.aysnc.AsyncProperties;
import com.eip.common.web.aysnc.WebAsyncConfig;
import com.eip.common.web.advice.GlobalExceptionHandler;
import com.eip.common.web.interceptor.GlobalLogInterceptor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

/**
 * ClassName: BaseAutoConfiguration
 * Function:
 * Date: 2022年01月10 13:16:02
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class BaseAutoConfiguration {


    //Feign调用配置
    //@Configuration
    //@ConditionalOnClass(FeignAutoConfiguration.class)
    //@EnableConfigurationProperties(FeignProperties.class)
    //@EnableFeignClients(basePackages = "com.eip",defaultConfiguration = FeignDefaultRequestInterceptor.class)
    //public static class FeignConfig{}

    /**
     * 当启用@EnableAsync注解时，才会启动自定义线程池
     */
    @Configuration
    @ConditionalOnBean(annotation = EnableAsync.class)
    @EnableConfigurationProperties(AsyncProperties.class)
    @Import(WebAsyncConfig.class)
    public static class AsyncConfig {
    }

    @Configuration
    @Import(ApiVersionWebConfiguration.class)
    public static class ApiConfig{
    }

    @Configuration
    @Import(GlobalLogInterceptor.class)
    public static class InterceptorConfig{}

    @Configuration
    @Import(SwaggerMvcConfigurer.class)
    public static class SwaggerConfig{}


    @Configuration
    @Import({GlobalResponseAdvice.class,GlobalExceptionHandler .class})
    public static class AdviceConfig{}

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("");
            }
        });
        return objectMapper;
    }
}
