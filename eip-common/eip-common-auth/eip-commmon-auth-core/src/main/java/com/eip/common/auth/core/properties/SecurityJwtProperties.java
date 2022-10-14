package com.eip.common.auth.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: SecurityJwtProperties
 * Function:
 * Date: 2022年09月26 15:20:42
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(prefix = "security.oauth2.jwt")
public class SecurityJwtProperties {

    /**
     * JWT签名RSA私钥
     */
    private String privateKey;

    /**
     * JWT签名RSA公钥
     */
    private String publicKey;

}
