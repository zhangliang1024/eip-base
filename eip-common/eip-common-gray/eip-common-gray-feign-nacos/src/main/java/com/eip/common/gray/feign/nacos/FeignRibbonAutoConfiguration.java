package com.eip.common.gray.feign.nacos;

import com.eip.common.gray.feign.nacos.aspect.FeignRibbonFilterAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: FeignRibbonAutoConfiguration
 * Function:
 * Date: 2023年01月04 17:17:47
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class FeignRibbonAutoConfiguration {

    @Bean
    public FeignRibbonFilterAspect feignRibbonFilterAspect() {
        return new FeignRibbonFilterAspect();
    }


}
