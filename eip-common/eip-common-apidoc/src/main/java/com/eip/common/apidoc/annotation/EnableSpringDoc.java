package com.eip.common.apidoc.annotation;

import com.eip.common.apidoc.config.SpringDocConfig;
import com.eip.common.apidoc.lisenter.ApplicationAccessUrlRunner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ClassName: EnableSpringDoc
 * Function: 开启Spring Doc文档
 * Date: 2022年09月23 16:45:16
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SpringDocConfig.class, ApplicationAccessUrlRunner.class})
public @interface EnableSpringDoc {


}
