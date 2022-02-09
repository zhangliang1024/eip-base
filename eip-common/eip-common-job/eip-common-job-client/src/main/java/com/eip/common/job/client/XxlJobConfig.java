package com.eip.common.job.client;

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
public class XxlJobConfig {

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

}
