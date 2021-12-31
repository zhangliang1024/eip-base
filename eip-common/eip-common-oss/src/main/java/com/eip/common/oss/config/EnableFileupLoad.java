package com.eip.common.oss.config;

import com.eip.common.oss.cloud.inter.CloudStorageFactory;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CloudStorageAutoConfig.class, CloudStorageFactory.class})
public @interface EnableFileupLoad {
}
