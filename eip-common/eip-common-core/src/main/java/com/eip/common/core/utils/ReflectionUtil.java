package com.eip.common.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * ClassName: ReflectionUtil
 * Function: 反射工具类
 * Date: 2021年12月08 10:46:53
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class ReflectionUtil {


    /**
     * Function: 通过反射获取方法
     * <p>
     * Date: 2021/12/8 10:50
     */
    public static Method getSpecificMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Function: 通过反射获取注解
     * <p>
     * Date: 2021/12/8 10:50
     */
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> clazz) {
        return method == null ? null : method.getAnnotation(clazz);
    }

}
