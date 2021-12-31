package com.eip.common.idempotent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoIdempotent
 * Function: 幂等注解
 *           1.接口层幂等校验，基于header中token。不支持重试
 *           2.如果出现错误，只能够换token进行
 * Date: 2021年12月07 16:16:48
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIdempotent {

}
