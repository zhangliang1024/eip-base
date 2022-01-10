package com.eip.common.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * ClassName: ApplicationContextUtils
 * Function:
 * Date: 2022年01月10 14:25:30
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class ApplicationContextUtils {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 静态的容器对象
     */
    private static ApplicationContext staticApplicationContext;

    @PostConstruct
    public void init(){
        ApplicationContextUtils.staticApplicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> c){
        if(!Objects.isNull(staticApplicationContext)){
            return staticApplicationContext.getBean(c);
        }
        return null;
    }


}
