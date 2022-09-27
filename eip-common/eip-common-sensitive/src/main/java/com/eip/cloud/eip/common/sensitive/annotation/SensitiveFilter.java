package com.eip.cloud.eip.common.sensitive.annotation;

import java.lang.annotation.*;

/**
 * ClassName: SensitiveFilter
 * Function: 敏感字符脱敏
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface SensitiveFilter {

    /**
     * 敏感字
     */
    String[] value() default {};
}
