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

    public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

    private static Class<?>[] EVENT_TYPES = {
            ApplicationEnvironmentPreparedEvent.class};

    private static final String LOG_NAME_KEY = "logging.file.name";

    private static final String APPLICATION_NAME = "spring.application.name";

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("监听器执行了！！！！");
        //获得当前系统环境配置对象
        ConfigurableEnvironment envi = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
        //获得配置源对象
        MutablePropertySources mps = envi.getPropertySources();

//        mps.stream().forEach(propertySource -> {
//            System.out.println("全局配置源名称：" + propertySource.getName());
//        });

        //从多个配置源对象中，获得系统属性配置对象
        PropertySource<?> ps = mps.get("configurationProperties");

        //并且从配置获取用户配置的日志文件名称，如果日志文件名称为null，则自动获取微服务名称
        if (ps != null) {
            String logName = (String) ps.getProperty(LOG_NAME_KEY);
            String logPath = (String) ps.getProperty(APPLICATION_NAME);
            if (StringUtils.isEmpty(logName)) {
                logName = (String) ps.getProperty(APPLICATION_NAME);
            }
//            System.out.println(logName + "  " + logPath);
            //将该属性放入Slf4j的全局属性对象当中
            MDC.put("logName", logName);
            MDC.put("logPath", logPath);
        }
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        //判断当前的事件类型是否为ApplicationEnvironmentPreparedEvent事件的子类类型，如果是的就会调用当前事件监听起
        return ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(resolvableType.getRawClass());
    }

}
