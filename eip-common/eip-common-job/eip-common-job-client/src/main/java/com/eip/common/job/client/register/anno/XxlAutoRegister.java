package com.eip.common.job.client.register.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: XxlAutoRegister
 * Function:
 * 默认调度类型: CRON
 * 默认运行模式：BEAN
 * Date: 2022年12月20 14:27:12
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XxlAutoRegister {

    /**
     * cron表达式
     */
    String cron();

    /**
     * 任务描述
     */
    String jobDesc() default "自定义任务";

    /**
     * 负责人
     */
    String author() default "default-auto-xxl-job";

    /**
     * 路由策略
     * 默认为 ROUND 轮询方式
     * 可选： FIRST 第一个
     */
    String executorRouteStrategy() default "ROUND";

    /**
     * 务的默认调度状态，0为停止状态，1为运行状态
     */
    int triggerStatus() default 0;

}
