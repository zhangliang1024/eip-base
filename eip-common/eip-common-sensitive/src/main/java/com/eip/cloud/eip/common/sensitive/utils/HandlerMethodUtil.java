package com.eip.cloud.eip.common.sensitive.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import lombok.experimental.UtilityClass;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * ClassName: HandlerMethodUtil
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@UtilityClass
public class HandlerMethodUtil {

    /**
     * 获取注解 从类和方法中
     */
    public static <T extends Annotation> T getAnnotation(HandlerMethod handlerMethod, Class<T> annotationType) {
        Class<?> beanType = handlerMethod.getBeanType();
        T annotation = AnnotationUtil.getAnnotation(beanType, annotationType);
        if (Objects.isNull(annotation)) {
            annotation = handlerMethod.getMethodAnnotation(annotationType);
        }
        return annotation;
    }
}
