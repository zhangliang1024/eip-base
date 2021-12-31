package com.eip.common.core.template.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: TransInfo
 * Function: 用于标记RestTemplate交互切面
 * Date: 2021年12月01 14:48:52
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TransInfo {

    /**
     * 功能模块
     */
    String module() default "";
    /**
     * 备注
     */
    String remark() default "";
}
