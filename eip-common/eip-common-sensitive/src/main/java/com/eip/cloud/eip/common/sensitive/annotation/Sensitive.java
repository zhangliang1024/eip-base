package com.eip.cloud.eip.common.sensitive.annotation;


import com.eip.cloud.eip.common.sensitive.enums.SensitiveTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    /**
     * 脱敏类型
     */
    SensitiveTypeEnum type();

    boolean required() default true;

}