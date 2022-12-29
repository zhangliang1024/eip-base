package com.eip.common.sms.wxchart.config;

import com.eip.common.sms.wxchart.service.WxBotSendService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version：V1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "eip.wx-chart", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(WxBotProperties.class)
public class WxBotAutoConfiguration {

    private final WxBotProperties wxChartProperties;

    public WxBotAutoConfiguration(final WxBotProperties wxChartProperties) {
        this.wxChartProperties = wxChartProperties;
    }

    @Bean
    public WxBotSendService wxBotService(){
        return new WxBotSendService(wxChartProperties.getWebhook());
    }
}
