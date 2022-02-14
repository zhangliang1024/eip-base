package com.eip.common.log.core.function;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: LogFunctionRegistrar
 * Function: SpringEL 注册自定义函数
 * Date: 2022年02月11 14:07:36
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class LogFunctionRegistrar implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static Map<String, Method> functionMap = new HashMap<>();

    /**
     * 扫描申明的自定义函数
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(LogFunction.class);
        beanWithAnnotation.values()
                .forEach(
                        component -> {
                            Method[] methods = component.getClass().getMethods();
                            LogFunction classLogFunction = component.getClass().getAnnotation(LogFunction.class);
                            String prefixName = classLogFunction.value();
                            if (StringUtils.hasText(prefixName)) {
                                prefixName += "_";
                            }
                            if (methods.length > 0) {
                                for (Method method : methods) {
                                    if (method.isAnnotationPresent(LogFunction.class) && isStaticMethod(method)) {
                                        LogFunction logFunction = method.getAnnotation(LogFunction.class);
                                        String registerName = StringUtils.hasText(logFunction.value()) ? logFunction.value() : method.getName();
                                        functionMap.put(prefixName + registerName, method);
                                        log.info("[operation-log] register function - [{}] as name [{}]", method, registerName);
                                    }
                                }
                            }
                        }
                );
    }

    /**
     * 判断是否为静态方法
     */
    private boolean isStaticMethod(Method method) {
        if (method == null) {
            return false;
        }
        int modifiers = method.getModifiers();
        return Modifier.isStatic(modifiers);
    }

    public static void register(StandardEvaluationContext context) {
        functionMap.forEach(context::registerFunction);
    }
}
