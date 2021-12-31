package com.eip.common.limit.generator;


import com.eip.common.limit.annotation.RedisParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 默认的Key生成器
 */
public class DefaultRedisKeyGenerator implements RedisKeyGenerator {

    @Override
    public String generate(String prefix, String delimiter, ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        final Object[] args = pjp.getArgs();
        final Parameter[] params = method.getParameters();

        StringBuilder sb = new StringBuilder();
        //默认解析方法里面带 RedisParam 注解的属性,如果没有尝试着解析实体对象中的
        for (int i = 0; i < params.length; i++) {
            final RedisParam annotation = params[i].getAnnotation(RedisParam.class);
            if (annotation == null) {
                continue;
            }
            sb.append(delimiter).append(args[i]);
        }
        if (StringUtils.isEmpty(sb.toString())) {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    final RedisParam annotation = field.getAnnotation(RedisParam.class);
                    if (annotation == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    sb.append(delimiter).append(ReflectionUtils.getField(field, object));
                }
            }
        }
        return prefix + sb.toString();
    }
}