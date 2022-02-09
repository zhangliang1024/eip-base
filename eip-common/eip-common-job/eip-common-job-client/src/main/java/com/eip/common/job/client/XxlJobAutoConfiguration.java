package com.eip.common.job.client;

import com.eip.common.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ConstantXxLJob
 * Function:
 * Date: 2021年12月06 17:08:00
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {XxlJobConfig.class})
@ConditionalOnProperty(prefix = ConstantXxLJob.XXL_JOB, name = ConstantXxLJob.ENABLED, havingValue = ConstantXxLJob.TRUE, matchIfMissing = true)
public class XxlJobAutoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobConfig jobConfig) {
        log.info("[eip-xxl-job] executor init ...");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobConfig.getAdminAddresses());
        xxlJobSpringExecutor.setAppName(jobConfig.getAppName() == null ? ConstantXxLJob.DEFAULT_APP_NAME : jobConfig.getAppName());
        xxlJobSpringExecutor.setIp(jobConfig.getIp());
        xxlJobSpringExecutor.setAddress(jobConfig.getAddress());
        xxlJobSpringExecutor.setPort(jobConfig.getPort());
        xxlJobSpringExecutor.setAccessToken(jobConfig.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobConfig.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(jobConfig.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }
}
