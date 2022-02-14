package com.eip.common.web.template;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: RestTemplateConfig
 * Function: 自定义配置RestTemplate
 * Date: 2021年12月01 14:34:08
 * <P>
 *    RestTemplate是Spring内置的http请求封装
 * </P>
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class RestTemplateConfig {

    @Autowired
    private OkHttpClient okHttpClient;

    @Bean
    public RestTemplate redisTemplate(RestTemplateBuilder builder) {
        return builder.customizers(restTemplateCustomizer())
                .additionalInterceptors(new CustomClientHttpRequestInterceptor())
                .errorHandler(new CustomErrorHandler())//自定义错误处理器
                .build();
    }


    /**
     * Function: 定制化RestTemplate
     * <p>
     * Date: 2021/12/1 14:31
     *
     * @author 张良
     * @param:
     * @return:
     */
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> {
            //设置请求工厂
            ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
            restTemplate.setRequestFactory(factory);
            /**
             * 解决请求回来数据乱码问题
             *
             * 1.StringHttpMessageConverter默认使用的字符集是ISO-8859-1
             * 2.用UTF-8 StringHttpMessageConverter来替代默认StringHttpMessageConverter
             */
            List<HttpMessageConverter<?>> newMessageConverters = new ArrayList<>();
            List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
            for (HttpMessageConverter<?> converter : messageConverters) {
                if (converter instanceof StringHttpMessageConverter) {
                    StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
                    newMessageConverters.add(messageConverter);
                } else {
                    newMessageConverters.add(converter);
                }
            }
            restTemplate.setMessageConverters(newMessageConverters);
        };
    }
}
