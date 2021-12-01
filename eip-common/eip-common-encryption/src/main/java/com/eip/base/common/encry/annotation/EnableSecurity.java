package com.eip.base.common.encry.annotation;

import com.eip.base.common.encry.advice.EncryptRequestBodyAdvice;
import com.eip.base.common.encry.advice.EncryptResponseBodyAdvice;
import com.eip.base.common.encry.config.SecretKeyConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动加解密
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({SecretKeyConfig.class,
        EncryptResponseBodyAdvice.class,
        EncryptRequestBodyAdvice.class})
public @interface EnableSecurity{

}
