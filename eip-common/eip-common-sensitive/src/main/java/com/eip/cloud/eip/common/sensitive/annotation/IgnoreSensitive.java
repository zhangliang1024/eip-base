package com.eip.cloud.eip.common.sensitive.annotation;

import java.lang.annotation.*;

/**
 * ClassName: IgnoreSensitive
 * Function: 忽略数据脱密逻辑
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreSensitive {

}
