package com.eip.common.config.apollo.listener;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.eip.common.config.apollo.service.ApolloListenerNamespace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * ClassName: EnablePropertiesEncryption
 * Function: Apollo 配置监听
 * 主要刷新：@ConfigurationProperties注解的bean
 * Date: 2022年01月12 18:22:25
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class ApolloConfigListener implements ApplicationEventPublisherAware, ApplicationContextAware {
    /**
     * 日志配置常量
     */
    private static final String LOGGING_LEVEL_PREFIX = "logging.level.";

    @Resource
    private LoggingSystem loggingSystem;
    @Autowired
    private ApolloListenerNamespace apolloListenerNamespace;

    private ApplicationContext applicationContext;
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


    @PostConstruct
    public void init() {
        initConfigUpdateListener();
    }

    /**
     * 初始化配置监听器
     */
    public void initConfigUpdateListener() {
        String[] namespaces = apolloListenerNamespace.value();
        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);
            config.addChangeListener(changeEvent -> {
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    log.info("[Apollo-config] - change -> propertyName= {} | oldValue= {} , newValue={} ",
                            change.getPropertyName(), change.getOldValue(), change.getNewValue());
                    if (StringUtils.containsIgnoreCase(key, LOGGING_LEVEL_PREFIX)) {
                        changeLoggingLevel(key, change);
                        continue;
                    }
                    // 更新相应的bean的属性值，主要是存在@ConfigurationProperties注解的bean
                    // rebind configuration beans（配置类无需要添加@RefreshScope）
                    this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
                    //发送刷新路由事件 -> 通知到CachingRouteLocator.onApplicationEvent
                    //this.publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        }
        log.info("[Apollo-Config-Listening] - inited");
    }


    /**
     * 刷新日志级别
     */
    private void changeLoggingLevel(String key, ConfigChange change) {
        if (null == loggingSystem) {
            return;
        }
        String newLevel = change.getNewValue();
        LogLevel level = LogLevel.valueOf(newLevel.toUpperCase());
        loggingSystem.setLogLevel(key.replace(LOGGING_LEVEL_PREFIX, ""), level);
    }
}

