package com.eip.common.datasource.annotation;

import java.lang.annotation.*;

/**
 * 特殊情况是某些情况下我们需要强制读主库，针对这种情况，我们定义一个主键，用该注解标注的就读主库
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Master {

}
