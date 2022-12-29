package com.eip.common.job.client.config;

import com.eip.common.job.client.register.service.JobGroupService;
import com.eip.common.job.client.register.service.JobInfoService;
import com.eip.common.job.client.register.service.JobLoginService;
import com.eip.common.job.client.register.XxlJobAutoRegister;
import com.eip.common.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
@EnableConfigurationProperties(value = {XxlJobAutoConfig.class})
@Import({XxlJobAutoRegister.class, JobGroupService.class, JobInfoService.class, JobLoginService.class})
@ConditionalOnProperty(prefix = ConstantXxLJob.XXL_JOB, name = ConstantXxLJob.ENABLED, havingValue = ConstantXxLJob.TRUE, matchIfMissing = true)
public class XxlJobAutoConfiguration {

    // 移除initMethod方法，否则会导致重复注册
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobAutoConfig jobConfig) {
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

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */
}
