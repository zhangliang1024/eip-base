package com.eip.common.log.core.function;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: LogFunction
 * Function: 自定义注册函数
 * Date: 2022年02月11 14:05:10
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface LogFunction {

    /**
     * 注册函数的function命名，便于自定义。若为空则默认使用 method的命名
     */
    String value() default "";

}
