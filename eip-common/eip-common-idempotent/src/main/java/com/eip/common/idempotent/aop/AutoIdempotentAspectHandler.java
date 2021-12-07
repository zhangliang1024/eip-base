package com.eip.common.idempotent.aop;

import com.eip.common.core.core.protocol.response.BaseResult;
import com.eip.common.idempotent.annotation.AutoIdempotent;
import com.eip.common.idempotent.service.TokenService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * ClassName: AutoIdempotentAspectHandler
 * Function:
 * Date: 2021年12月07 16:18:40
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AutoIdempotentAspectHandler {

    @Autowired
    private TokenService tokenService;

    @Pointcut("@annotation(com.eip.common.idempotent.annotation.AutoIdempotent)")
    public void pointcut(){
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Method method = getMethod(joinPoint);
        AutoIdempotent autoIdempotent = method.getAnnotation(AutoIdempotent.class);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =attributes.getRequest();
        if(tokenService.checkToken(request)){
            return joinPoint.proceed();
        }
        return new BaseResult();
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method;
    }

}
