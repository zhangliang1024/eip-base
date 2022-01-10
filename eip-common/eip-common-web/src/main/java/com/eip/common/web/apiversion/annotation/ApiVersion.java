package com.eip.common.web.apiversion.annotation;

import java.lang.annotation.*;

/**
 * ClassName: ApiVersion
 * Function: 接口多版本注解，可用于方法和类
 * Date: 2022年01月10 13:25:20
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ApiVersion {

    /**
     * 版本号
     */
    double value();

}
