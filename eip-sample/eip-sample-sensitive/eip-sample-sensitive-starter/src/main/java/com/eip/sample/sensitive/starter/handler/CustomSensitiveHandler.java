package com.eip.sample.sensitive.starter.handler;

import com.eip.cloud.eip.common.sensitive.annotation.Sensitive;
import com.eip.cloud.eip.common.sensitive.handler.SensitiveWrapper;
import com.eip.cloud.eip.common.sensitive.strategy.DefaultSensitiveHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * ClassName: CustomSensitiveHandler
 * Function:
 * Date: 2022年09月27 17:19:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class CustomSensitiveHandler extends DefaultSensitiveHandler {


    @Override
    public String customHandler(SensitiveWrapper sensitiveWrapper) {
        Field field = sensitiveWrapper.getField();   // 字段
        Class<?> objectClass = field.getDeclaringClass();  // 字段归属的对象
        Annotation[] annotations = field.getAnnotations();   // 字段上的注解
        String fieldValue = sensitiveWrapper.getFieldValue();  // 字段值
        Sensitive sensitive = sensitiveWrapper.getSensitive();   // 注解信息
        return "@" + fieldValue + "@";
    }
}
