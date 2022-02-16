package com.eip.sample.log.function;

import com.eip.common.log.core.function.LogFunction;
import com.eip.sample.log.service.LogService;
import com.eip.sample.log.util.SpringContextUtils;

/**
 * SpEL自定义函数
 */
@LogFunction
public class LogFuction {

    private static LogService logService;

    @LogFunction
    public static String function(String str) {
        if (logService == null) {
            logService = SpringContextUtils.getBean(LogService.class);
        }
        return logService.serviceFunc2(str);
    }
}
