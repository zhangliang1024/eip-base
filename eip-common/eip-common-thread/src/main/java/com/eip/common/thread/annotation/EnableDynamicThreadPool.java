package com.eip.common.thread.annotation;


import com.eip.common.thread.config.DynamicThreadPoolAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 动态线程池配置类启动注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DynamicThreadPoolAutoConfiguration.class})
@Documented
@Inherited
public @interface EnableDynamicThreadPool {

}
