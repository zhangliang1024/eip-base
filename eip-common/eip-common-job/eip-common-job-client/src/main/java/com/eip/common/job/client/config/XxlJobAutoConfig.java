package com.eip.common.job.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: ConstantXxLJob
 * Function:
 * Date: 2021年12月06 17:08:00
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(ConstantXxLJob.XXL_JOB)
public class XxlJobAutoConfig {

    private Boolean enabled = false;

    private String adminAddresses;

    @Value("${spring.application.name}")
    private String appName;

    private String ip = ConstantXxLJob.DEFAULT_IP;

    private String address;

    private int port = ConstantXxLJob.PORT;

    private String accessToken;

    private String logPath = ConstantXxLJob.LOG_PATH;

    private int logRetentionDays = ConstantXxLJob.LOG_RETENTION_DAYS;

    /*---------------------------自动注册执行器---------------------------*/
    /**
     * xxl-job admin 用户名
     */
    private String username;
    /**
     * xxl-job admin 密码
     */
    private String password;
    /**
     * 执行器名称
     */
    private String title;

}
