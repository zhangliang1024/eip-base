package com.eip.common.web.apiversion.annotation;

import com.eip.common.web.apiversion.config.ApiVersionWebConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ClassName: EnableApiVersion
 * Function: 自定义接口多版本启动注解
 * Date: 2022年01月10 14:11:12
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Documented
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Import(ApiVersionWebConfiguration.class)
public @interface EnableApiVersion {

}
