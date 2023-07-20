package com.eip.common.base.rocketmq.config;

import com.eip.common.base.rocketmq.constant.EnhanceMessageConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: RocketEnhanceProperties
 * Function:
 * Date: 2023年07月20 15:14:10
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(prefix = "rocketmq.enhance")
public class RocketEnhanceProperties {

    /**
     * 启动隔离，用于激活配置类EnvironmentIsolationConfig
     * 启动后会自动在topic上拼接激活的配置文件，达到自动隔离的效果
     */
    private boolean enabledIsolation;
    /**
     * 隔离环境名称，拼接到topic后，topic_dev，默认空字符串
     */
    private String environment;
    /**
     * 灰度标识，默认：""
     */
    private String grayFlag = EnhanceMessageConstant.DEFAULT_GRAY_FLAG;

}