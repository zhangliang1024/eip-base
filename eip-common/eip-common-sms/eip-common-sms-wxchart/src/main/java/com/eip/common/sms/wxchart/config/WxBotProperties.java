package com.eip.common.sms.wxchart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @类描述：企业微信配置
 * @version：V1.0
 */
@Component
@ConfigurationProperties(prefix = "eip.wx-chart")
public class WxBotProperties {

    /**
     * 超时时间（默认5秒）
     */
    public static final int DEFAULT_TIMEOUT = 5 * 1000;

    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * webhook地址
     */
    private String webhook;
    /**
     * 超时时间
     */
    private int timeout = DEFAULT_TIMEOUT;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
