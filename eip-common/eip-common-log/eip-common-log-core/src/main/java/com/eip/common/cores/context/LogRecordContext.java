package com.eip.common.cores.context;

import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * ClassName: LogRecordContext
 * Function:
 * Date: 2022年02月11 13:33:55
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class LogRecordContext {

    private static final ThreadLocal<StandardEvaluationContext> CONTEXT_THREAD_LOCAL = new NamedThreadLocal<>("ThreadLocal StandardEvaluationContext");

    public static StandardEvaluationContext getContext(){
        return CONTEXT_THREAD_LOCAL.get() == null ? new StandardEvaluationContext() : CONTEXT_THREAD_LOCAL.get();
    }

    public static void putVariables(String key,String value){
        StandardEvaluationContext context = getContext();
        context.setVariable(key,value);
        CONTEXT_THREAD_LOCAL.set(context);
    }

    public static void clearContext(){
        CONTEXT_THREAD_LOCAL.remove();
    }
}
