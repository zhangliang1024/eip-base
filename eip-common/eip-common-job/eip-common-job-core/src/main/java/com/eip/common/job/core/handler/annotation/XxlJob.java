package com.eip.common.job.core.handler.annotation;

import java.lang.annotation.*;

/**
 * annotation for method jobhandler
 *
 * @author xuxueli 2019-12-11 20:50:13
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XxlJob {

    /**
     * jobhandler name
     */
    String value();

    /**
     * init advice, invoked when JobThread init
     */
    String init() default "";

    /**
     * destroy advice, invoked when JobThread destroy
     */
    String destroy() default "";

}
