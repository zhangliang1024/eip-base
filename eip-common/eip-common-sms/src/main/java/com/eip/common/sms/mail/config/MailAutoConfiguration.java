package com.eip.common.sms.mail.config;

import com.eip.common.sms.mail.service.MailSendService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: MailAutoConfiguration
 * Function: sms配置
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "eip.mail", name = "enabled", havingValue = "true")
@ComponentScan(basePackages = "com.eip.common.sms.mail")
public class MailAutoConfiguration {

    @Bean
    public MailSendService mailSendService(){
        return new MailSendService();
    }

}
