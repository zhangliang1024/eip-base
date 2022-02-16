package com.eip.common.core.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
public class ApplicationContextUtil {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 静态的容器对象
     */
    private static ApplicationContext staticApplicationContext;

    @PostConstruct
    public void init(){
        ApplicationContextUtil.staticApplicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return staticApplicationContext;
    }


    /**
     * 通过class获取Bean.
     *
     * @param clazz
     */
    public static <T> T getBean(Class<T> clazz){
        if(!Objects.isNull(staticApplicationContext)){
            return staticApplicationContext.getBean(clazz);
        }
        return null;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }


    /**
     * 通过name及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
