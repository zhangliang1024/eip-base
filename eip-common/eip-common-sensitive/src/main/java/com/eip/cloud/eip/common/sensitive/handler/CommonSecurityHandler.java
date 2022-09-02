package com.eip.cloud.eip.common.sensitive.handler;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.eip.cloud.eip.common.sensitive.annotation.Security;
import com.eip.cloud.eip.common.sensitive.codec.SecurityProcessor;
import com.eip.cloud.eip.common.sensitive.enums.SecurityMode;
import lombok.Data;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

@Data
public class CommonSecurityHandler implements SecurityHandler<Security> {

    private SecurityProcessor securityProcessor;

    private SecurityMode securityMode;

    private Charset charset = Charset.forName("UTF-8");

    public CommonSecurityHandler(SecurityProcessor securityProcessor, SecurityMode securityMode) {
        Assert.notNull(securityProcessor, "securityProcessor could not be null");
        this.securityProcessor = securityProcessor;
        this.securityMode = securityMode;
    }

    @Override
    public boolean support(Field field) {
        Security annotation = field.getAnnotation(Security.class);
        return annotation != null && annotation.required();
    }

    @Override
    public Security acquire(Field field) {
        Security annotation = null;
        if (field != null) {
            annotation = field.getAnnotation(Security.class);
        }
        return annotation;
    }

    @Override
    public String handleEncrypt(String source, Security annotation) {
        if (annotation.required()) {
            byte[] encrypt = securityProcessor.encrypt(source.getBytes(this.charset));
            switch (securityMode) {
                case BASE64:
                    return Base64Encoder.encode(encrypt);
                case HEX:
                    return HexUtil.encodeHexStr(encrypt);
                 default:
                     break;
            }
        }
        return source;
    }

    @Override
    public String handleDecrypt(String source, Security annotation) {
        if (annotation.required()) {
            byte[] decrypt = securityProcessor.decrypt(source);
            return StrUtil.str(decrypt, charset);
        }
        return source;
    }
}