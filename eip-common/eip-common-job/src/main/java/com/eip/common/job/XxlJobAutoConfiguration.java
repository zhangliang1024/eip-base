package com.eip.common.job;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-JOB 的自动装配
 *
 * @author Levin
 * @since 2019/08/06
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {XxlJobProperties.class})
@ConditionalOnProperty(prefix = ConstantXxLJob.XXL_JOB, name = ConstantXxLJob.ENABLED, havingValue = ConstantXxLJob.TRUE, matchIfMissing = true)
public class XxlJobAutoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties properties) {
        log.info(">>>>>>>>>>> xxl-job filter init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(properties.getAdminAddresses());
        xxlJobSpringExecutor.setAppName(properties.getAppName());
        xxlJobSpringExecutor.setIp(properties.getIp());
        xxlJobSpringExecutor.setPort(properties.getPort());
        xxlJobSpringExecutor.setAccessToken(properties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(properties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(properties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }
}
