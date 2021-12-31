package com.eip.common.core.template;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            //创建连接池管理器 默认支持HTTP HTTPS
            PoolingHttpClientConnectionManager pm = new PoolingHttpClientConnectionManager();
            //最大连接数
            pm.setMaxTotal(100);
            //同路由并发数
            pm.setDefaultMaxPerRoute(20);
            //
            pm.setValidateAfterInactivity(2000);


            //创建HttpClient
            HttpClient httpClient = httpClientBuilder.build();

            //TODO okHttp
            //OkHttp3ClientHttpRequestFactory okFactory = new OkHttp3ClientHttpRequestFactory();

            //HttpComponentsClientHttpRequestFactory
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            //连接超时时间: 连接上服务器(握手成功)的时间
            factory.setConnectTimeout(10 * 1000);
            //读取数据超时：服务器返回数据时间
            factory.setReadTimeout(60 * 1000);
            //连接不够用等待时间：从连接池获取连接超时时间
            factory.setConnectionRequestTimeout(30 * 1000);

            //设置请求工厂
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
