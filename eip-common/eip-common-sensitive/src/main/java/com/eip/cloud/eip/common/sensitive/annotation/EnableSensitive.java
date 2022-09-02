package com.eip.cloud.eip.common.sensitive.annotation;

import com.eip.cloud.eip.common.sensitive.config.SensitiveConfigurationSelector;
import com.eip.cloud.eip.common.sensitive.config.SensitivePropertiesAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SensitivePropertiesAutoConfiguration.class, SensitiveConfigurationSelector.class})
public @interface EnableSensitive {

    boolean security() default true;

    boolean sensitive() default true;

    String[] packages() default {};

}