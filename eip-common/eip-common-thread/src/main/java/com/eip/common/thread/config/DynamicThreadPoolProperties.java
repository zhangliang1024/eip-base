package com.eip.common.thread.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 动态线程池配置
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "eip.dynamic.thread.pool")
public class DynamicThreadPoolProperties {


    /***
     * Nacos DataId 监听配置变更使用
     */
    private String nacosDataId;
    /**
     * Nacos Group 监听配置变更使用
     */
    private String nacosGroup;
    /**
     * Nacos 等待配置刷新时间(监听器收到消息变更同时，此时Spring容器中的配置bean还没更新，需要等待固定的时间)
     * ★ 实测结果
     * 1.Nacos的配置也通过手动来做实时刷新
     * 2.Apollo通过客户端获取的配置，会存在延时问题。现在猜测通过客户端获取配置时，远程的配置还没有更新到本地，导致获配置延时
     *   故增加延时等待操作和控制开关
     * 3.单位：秒
     */
    private int waitRefreshConfigSeconds = ThreadPoolConstant.DEFAULT_NACOS_WAIT_REFRESH_CONFIG_SECONDS;
    /**
     * 是否等待配置刷新
     */
    private boolean boolWaitRefreshConfig = ThreadPoolConstant.DEFAULT_IS_WAIT_REFRESH_CONFIG;
    /**
     * Apollo的namespace 监听配置变更使用
     */
    private String apolloNamespace;

    /**
     * 线程池配置
     */
    private List<ThreadPoolProperties> executors = new ArrayList<>();


    /**
     * 负责人
     */
    private String owner;
    /**
     * 异常告警API地址，如果配置了此值就将异常信息告诉配置的API，异常告警由外部系统处理
     * 默认会通过钉钉告警，需要配置钉钉信息
     */
    private String alarmApiUrl;

    /**
     * 钉钉机器人access_token
     */
    private String accessToken;
    /**
     * 钉钉机器儿secret
     */
    private String secret;
    /**
     * 告警时间间隔，单位分钟
     */
    private int alarmTimeInterval = ThreadPoolConstant.DEFAULT_ALARM_TIME_INTERVAL;


    /**
     * 刷新配置
     * content 整个配置文件
     */
    public void refresh(String content){
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(content.getBytes()));
        } catch (IOException e) {
            log.error("转换Properties异常", e);
        }
        doRefresh(properties);
    }

    public void refreshYaml(String content){
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(new ByteArrayResource(content.getBytes()));
        Properties properties = bean.getObject();
        doRefresh(properties);
    }


    private void doRefresh(Properties properties) {
        HashMap<String, String> dataMap = new HashMap<>((Map)properties);
        MapConfigurationPropertySource source = new MapConfigurationPropertySource(dataMap);
        Binder binder = new Binder(source);
        binder.bind("eip.dynamic.thread.pool", Bindable.ofInstance(this)).get();
    }
}
