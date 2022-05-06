package com.eip.common.web.valid.annotaion;

import com.eip.common.web.valid.extend.ValidHandler;

import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * ClassName: CustomVaild
 * Function:
 * Date: 2022年01月10 14:19:03
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Documented
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CustomVaild {


    String message() default "校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 指定实际处理的校验器
     */
    Class<? extends ValidHandler> handler();
}
