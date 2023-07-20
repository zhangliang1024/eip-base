package com.eip.common.base.rocketmq.config;

import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * ClassName: EnvironmentIsolationConfig
 * Function: 实现环境隔离配置
 * Date: 2023年07月20 15:14:10
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class EnvironmentIsolationConfig implements BeanPostProcessor {

    private RocketEnhanceProperties rocketEnhanceProperties;

    public EnvironmentIsolationConfig(RocketEnhanceProperties rocketEnhanceProperties) {
        this.rocketEnhanceProperties = rocketEnhanceProperties;
    }

    /**
     * 在监听器实例初始化前修改对应的topic
     * 在装载Bean之前实现参数修改
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
            if (rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())) {
                String extendTopic = rocketEnhanceProperties.getEnvironment();
                if (StringUtils.hasText(rocketEnhanceProperties.getGrayFlag())) {
                    extendTopic = String.join("_", extendTopic, rocketEnhanceProperties.getGrayFlag());
                }
                container.setTopic(String.join("_", container.getTopic(), extendTopic));
            }
            return container;
        }
        return bean;
    }
}