package com.eip.cloud.eip.common.sensitive.config;

import com.eip.cloud.eip.common.sensitive.format.CommonDesensitize;
import com.eip.cloud.eip.common.sensitive.handler.SecurityHandler;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveHandler;
import com.eip.cloud.eip.common.sensitive.properties.SensitiveConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SensitiveAutoConfiguration extends AbstractConfiguration{

    public SensitiveAutoConfiguration(SensitiveConfigProperties configProperties) {
       super(configProperties);
    }

    @Bean
    public SecurityHandler sensitiveHandler() {
        SensitiveHandler sensitiveHandler = new SensitiveHandler();
        sensitiveHandler.setDesensitize(new CommonDesensitize());
        return sensitiveHandler;
    }

}