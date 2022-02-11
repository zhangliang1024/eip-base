package com.eip.common.log.core.config;

import com.eip.common.log.core.aspect.LogOperationLogAspect;
import com.eip.common.log.core.function.LogFunctionRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * ClassName: LogAutoConfiguration
 * Function:
 * Date: 2022年02月11 14:31:50
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@ComponentScan("com.eip.common.log.core")
@Import({RocketMqSenderConfig.class})
public class LogAutoConfiguration {


    @Bean
    LogOperationLogAspect logOperationLogAspect(){
        return new LogOperationLogAspect();
    }

    @Bean
    LogFunctionRegistrar LogFunctionRegistrar(){
        return new LogFunctionRegistrar();
    }
}
