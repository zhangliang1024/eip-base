package com.eip.base.common.log;

import org.slf4j.MDC;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * 自定义的一个SpringBoot的事件监听对象
 * 监听事件 - ApplicationEnvironmentPreparedEvent - 系统环境准备完成的事件
 */
public class ApplicationStartEventListener implements GenericApplicationListener {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 19;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    //判断当前的事件类型是否为ApplicationEnvironmentPreparedEvent事件的子类类型，如果是的就会调用当前事件监听起
    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(resolvableType.getRawClass());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("[日志] - 监听器执行了！！！！");
        //转换成环境的事件对象
        ApplicationEnvironmentPreparedEvent environmentPreparedEvent = (ApplicationEnvironmentPreparedEvent) event;

        //获取当前的环境信息
        ConfigurableEnvironment environment = environmentPreparedEvent.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        PropertySource<?> propertySource = propertySources.get("configurationProperties");

        //获取微服务的配置文件中 应用的名称
        String serverName = (String) propertySource.getProperty("spring.application.name");
        String logName = (String) propertySource.getProperty("logging.file.name");
        String logPath = (String) propertySource.getProperty("logging.file.path");

        //封装到MDC容器中
        MDC.put("logName", StringUtils.isEmpty(logName) ? serverName : logName);
        MDC.put("logPath", StringUtils.isEmpty(logPath) ? serverName : logPath);
    }

}
