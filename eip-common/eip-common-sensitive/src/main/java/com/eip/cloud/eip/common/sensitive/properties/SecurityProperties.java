package com.eip.cloud.eip.common.sensitive.properties;

import lombok.Data;

@Data
public class SecurityProperties {

    /**
     * @See SecurityMode
     */
    private String mode = "BASE64";

    private String type = "AES";

    private String charset = "UTF-8";

    private String secret = "+6cuvzvyrFZpRG9pf3r7eQ==";

    private String privateKey;

    private String publicKey;
}