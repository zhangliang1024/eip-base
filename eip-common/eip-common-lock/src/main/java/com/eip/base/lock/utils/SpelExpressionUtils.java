package com.eip.base.lock.utils;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class SpelExpressionUtils {

    /**
     * 创建Spel表达式解析器
     */
    private static ExpressionParser parser = new SpelExpressionParser();

    /**
     * 方法参数名称解析器
     */
    private static LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 解析方法
     * @param <T>
     * @return
     */
    public static <T> T parserSpel(Method method, Object[] params, String key, Class<T> resultType, T defaultValue){

        //解析出方法的参数名称
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        //创建标准的求值上下文对象
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        //循环所有参数
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], params[i]);
        }
        try {
            //执行表达式解析
            Expression expression = parser.parseExpression(key);
            T value = expression.getValue(evaluationContext, resultType);
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
