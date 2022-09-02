package com.eip.cloud.eip.common.sensitive.config;

import com.eip.cloud.eip.common.sensitive.codec.*;
import com.eip.cloud.eip.common.sensitive.enums.SecurityMode;
import com.eip.cloud.eip.common.sensitive.handler.CommonSecurityHandler;
import com.eip.cloud.eip.common.sensitive.handler.SecurityHandler;
import com.eip.cloud.eip.common.sensitive.properties.SensitiveConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

@Slf4j
@Configuration
public class SecurityAutoConfiguration extends AbstractConfiguration{

    public static final String BASE64 = "BASE64";

    public static final String HEX = "HEX";

    public SecurityAutoConfiguration(SensitiveConfigProperties configProperties) {
        super(configProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.sensitive.security", name = "type", havingValue = "AES", matchIfMissing = true)
    public SecurityProcessor aesProcessor(SensitiveConfigProperties configProperties) {
        return new AESProcessor(configProperties.getSecurity().getSecret());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.sensitive.security", name = "type", havingValue = "RSA")
    public SecurityProcessor rsaProcessor(SensitiveConfigProperties configProperties) {
        return new RSAProcessor(configProperties.getSecurity().getPublicKey(), configProperties.getSecurity().getPrivateKey());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.sensitive.security", name = "type", havingValue = "DES")
    public SecurityProcessor desProcessor(SensitiveConfigProperties configProperties) {
        return new DESProcessor(configProperties.getSecurity().getSecret());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.sensitive.security", name = "type", havingValue = "SM4")
    public SecurityProcessor sm4Processor(SensitiveConfigProperties configProperties) {
        return new SM4Processor(configProperties.getSecurity().getSecret());
    }


    @Bean
    public SecurityHandler commonSecurityHandler(SecurityProcessor securityProcessor, SensitiveConfigProperties configProperties) {
        String charsetName = configProperties.getSecurity().getCharset();
        String mode = configProperties.getSecurity().getMode();
        SecurityMode securityMode = null;
        try {
            Charset charset = Charset.forName(charsetName);
            if (BASE64.equalsIgnoreCase(mode)) {
                securityMode = SecurityMode.BASE64;
            } else if (HEX.equalsIgnoreCase(mode)) {
                securityMode = SecurityMode.HEX;
            } else {
                throw new RuntimeException("Unsupported mode " + mode);
            }
            CommonSecurityHandler securityHandler = new CommonSecurityHandler(securityProcessor, securityMode);
            securityHandler.setCharset(charset);
            return securityHandler;
        } catch (UnsupportedCharsetException e) {
            log.error("Unsupported charset name {}", charsetName);
            throw e;
        }
    }
}