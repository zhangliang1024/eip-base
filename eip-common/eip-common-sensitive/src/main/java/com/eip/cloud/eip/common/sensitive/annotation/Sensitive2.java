package com.eip.cloud.eip.common.sensitive.annotation;

import com.eip.cloud.eip.common.sensitive.enums.SensitiveTypeEnum;
import com.eip.cloud.eip.common.sensitive.format.CommonDesensitize;
import com.eip.cloud.eip.common.sensitive.serializer.SensitiveSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
@Documented
public @interface Sensitive2 {

    /**
     * 脱敏类型
     */
    SensitiveTypeEnum type();

    /**
     * 自定义规则
     */
    Class<?> value() default CommonDesensitize.class;
}