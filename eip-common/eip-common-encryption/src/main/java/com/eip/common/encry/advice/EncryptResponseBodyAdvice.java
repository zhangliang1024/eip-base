package com.eip.common.encry.advice;

import com.eip.common.core.utils.JacksonUtil;
import com.eip.common.core.utils.hutool.HutoolRSAUtils;
import com.eip.common.encry.annotation.Encrypt;
import com.eip.common.encry.config.SecretKeyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {


    private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();


    private boolean encrypt;

    @Autowired
    private SecretKeyConfig config;


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        encrypt = false;
        if (returnType.getMethod().isAnnotationPresent(Encrypt.class) && config.isOpen()) {
            encrypt = true;
        }
        return encrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        Boolean status = encryptLocal.get();
        if (null != status && !status) {
            encryptLocal.remove();
            return body;
        }
        if (encrypt && !config.isDebug()) {
            String privateKey = config.getPrivateKey();
            try {
                String content = JacksonUtil.objectToStr(body);
                if (!StringUtils.hasText(privateKey)) {
                    throw new NullPointerException("Please configure rsa.encrypt.privatekeyc parameter!");
                }
                String result = HutoolRSAUtils.encryptByPrivate(content, privateKey);
                if (config.isShowLog()) {
                    log.info("Pre-encrypted data：{}，After encryption：{}", content, result);
                }
                return result;
            } catch (Exception e) {
                log.error("Encrypted data exception", e);
            }
        }
        return body;
    }
}
