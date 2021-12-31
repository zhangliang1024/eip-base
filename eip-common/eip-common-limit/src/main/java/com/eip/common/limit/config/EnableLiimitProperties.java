package com.eip.common.limit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pzy.redis.limit")
public class EnableLiimitProperties {

    /**
     * 是否开启 @RedisLimit 的拦截器功能
     */
    private boolean enabled = true;
    /**
     * 限流类型：local redis
     */
    private String type = "local";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
