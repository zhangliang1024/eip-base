package com.eip.cloud.eip.common.sensitive.annotation;

import com.eip.cloud.eip.common.sensitive.enums.SensitiveStrategy;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveHandler;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveConstants;
import com.eip.cloud.eip.common.sensitive.serializer.SensitiveSerializer;
import com.eip.cloud.eip.common.sensitive.strategy.DefaultSensitiveHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * ClassName: Sensitive
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
public @interface Sensitive {

    /**
     * 数据脱敏策略
     */
    SensitiveStrategy type() default SensitiveStrategy.CUSTOMIZE_HANDLER;

    /**
     * 用什么打码
     */
    char symbol() default SensitiveConstants.REPLACER;

    /**
     * 前置不需要打码的长度
     */
    int prefix() default SensitiveConstants.NO_MASK_LEN;

    /**
     * 后置不需要打码的长度
     */
    int suffix() default SensitiveConstants.NO_MASK_LEN;

    /**
     * 数据脱密处理
     */
    Class<? extends SensitiveHandler> value() default DefaultSensitiveHandler.class;
}
