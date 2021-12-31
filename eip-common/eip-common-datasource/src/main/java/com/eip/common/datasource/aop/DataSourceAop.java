package com.eip.common.datasource.aop;

import com.eip.common.datasource.context.DBContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAop {

    /**
     * 只读：
     * 不是Master注解的对象或方法  && select开头的方法  ||  get开头的方法
     */
    @Pointcut("!@annotation(com.eip.common.datasource.annotation.Master)"+
            "&&(execution(* com..*Impl.select*(..))"+
            "|| execution(* com..*Impl.get*(..))" +
            "|| execution(* com..*Impl.find*(..)))")
    public void readPointcut(){
    }

    /**
     * 写：
     * Master注解的对象或方法 || insert开头的方法  ||  add开头的方法 || update开头的方法
     * || edlt开头的方法 || delete开头的方法 || remove开头的方法
     */
    @Pointcut("@annotation(com.eip.common.datasource.annotation.Master) " +
            "|| execution(* com..*Impl.insert*(..)) " +
            "|| execution(* com..*Impl.add*(..)) " +
            "|| execution(* com..*Impl.update*(..)) " +
            "|| execution(* com..*Impl.edit*(..)) " +
            "|| execution(* com..*Impl.delete*(..)) " +
            "|| execution(* com..*Impl.remove*(..))"+
            "|| execution(* com..*Impl.save*(..))")
    public void writePointcut(){
    }

    @Before("readPointcut()")
    public void read(){
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write(){
        DBContextHolder.master();
    }

}
