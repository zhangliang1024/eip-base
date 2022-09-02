package com.eip.cloud.eip.common.sensitive.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Security {

    /**
     * SecurityEnum type() default SecurityEnum.AES;
     */
    boolean required() default true;

}