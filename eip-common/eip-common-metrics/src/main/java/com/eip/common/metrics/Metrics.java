package com.eip.common.metrics;

import java.lang.annotation.*;

/**
 * ClassName: Metrics
 * Function: 监控注解，可用于方法、类
 * Date: 2021年12月17 15:23:07
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Metrics {


}
