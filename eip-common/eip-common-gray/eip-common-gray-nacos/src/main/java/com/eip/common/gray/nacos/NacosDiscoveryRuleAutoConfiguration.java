package com.eip.common.gray.nacos;

import com.alibaba.cloud.nacos.ribbon.NacosServerList;
import com.eip.common.gray.core.rule.DiscoveryEnabledRule;
import com.eip.common.gray.feign.aspect.FeignRibbonFilterAspect;
import com.eip.common.gray.nacos.rule.NacosMetadataAwareRule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * ClassName: RibbonDiscoveryRuleAutoConfiguration
 * Function:
 * Date: 2023年01月04 14:48:09
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@ConditionalOnClass(NacosServerList.class)
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.nacos.filter.metadata.enabled", havingValue = "true", matchIfMissing = true)
public class NacosDiscoveryRuleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DiscoveryEnabledRule discoveryEnabledRule() {
        return new NacosMetadataAwareRule();
    }

    @Bean
    public FeignRibbonFilterAspect feignRibbonFilterAspect() {
        return new FeignRibbonFilterAspect();
    }
}
