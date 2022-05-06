package com.eip.common.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * ClassName: EipEnableFeignClients
 * Function: 自定义Feign注解
 * Date: 2022年01月17 14:36:12
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EipEnableFeignClients {

    String[] value() default {};

    String[] basePackages() default {"com.eip.business.feign"};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};

}
