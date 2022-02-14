package com.eip.common.log.core.annotation;

import com.eip.common.log.core.enums.LogEventTypeEnum;
import com.eip.common.log.core.enums.LogLevelEnum;
import com.eip.common.log.core.enums.LogOperateTypeEnum;

import java.lang.annotation.*;

/**
 * ClassName: LogOperation
 * Function: 操作日志
 * Date: 2022年02月11 11:03:55
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {

    /**
     * 日志级别
     */
    LogLevelEnum level() default LogLevelEnum.LOW;

    /**
     * 事件类型
     */
    LogEventTypeEnum eventType() default LogEventTypeEnum.BUSINESS;

    /**
     * 操作类型
     */
    LogOperateTypeEnum operateType() default LogOperateTypeEnum.OPERATION;

    /**
     * 操作模块
     */
    String operateModule() default "";

    /**
     * 业务日志ID
     */
    String businessId();

    /**
     * 业务类型
     */
    String businessType();

    /**
     * 日志message
     */
    String logMessage() default "";

}
