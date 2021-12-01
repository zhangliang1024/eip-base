package com.eip.base.common.encry.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "eip.encry.rsa")
public class SecretKeyConfig{

    private String privateKey;

    private String publicKey;

    private String charset = "UTF-8";

    private boolean open = true;
    /**
     * 是否打印debug日志
     */
    private boolean showLog = false;
    /**
     * 开启调试模式，调试模式下不进行加解密操作，用于像Swagger这种在线API测试场景
     */
    private boolean debug = false;
}
