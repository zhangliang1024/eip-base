package com.eip.common.gray.feign.nacos.aspect;

import com.eip.common.gray.core.handler.GrayServiceHandlerChain;
import com.eip.common.gray.core.support.RibbonFilterContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * ClassName: FeignRibbonFilterAspect
 * Function:
 * Date: 2023年01月04 15:32:40
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Aspect
public class FeignRibbonFilterAspect {

    @Autowired
    private GrayServiceHandlerChain handlerChain;

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
    public void pointcut(){}

    /**
     * 执行目标方法之前调用
     */
    @Before("pointcut()")
    public void beforeHandle(JoinPoint joinPoint){
        String serviceId = this.getFeignClientName(joinPoint);
        if(StringUtils.isBlank(serviceId)){
            return;
        }
        handlerChain.handle(serviceId);
    }

    public String getFeignClientName(JoinPoint joinPoint){
        Class<?>[] interfaces = joinPoint.getTarget().getClass().getInterfaces();
        for (Class<?> clazz : interfaces) {
            Annotation annotation = clazz.getAnnotation(FeignClient.class);
            if(Objects.nonNull(annotation) && annotation instanceof FeignClient){
                FeignClient feignClient = (FeignClient) annotation;
                return feignClient.name();
            }
        }
        return null;
    }

    /**
     * 执行目标方法之后调用
     */
    @After("pointcut()")
    public void afterHande(){
        RibbonFilterContextHolder.clearCurrentContext();
    }
}
