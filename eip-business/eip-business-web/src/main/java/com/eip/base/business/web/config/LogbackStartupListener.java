package com.eip.base.business.web.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * ClassName: LogbackStartupListener
 * Function: Logback监听器
 *  Springboot Logback日志使用，Springboot Logback详细配置和日志分割：https://www.cnblogs.com/fanshuyao/p/14177544.html
 * Date: 2021年12月17 13:57:33
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class LogbackStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {


    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext loggerContext) {

    }

    @Override
    public void onReset(LoggerContext loggerContext) {

    }

    @Override
    public void onStop(LoggerContext loggerContext) {

    }

    @Override
    public void onLevelChange(Logger logger, Level level) {

    }

    @Override
    public void start() {
        String logPath = System.getProperty("logging.file.path");
        String logName = System.getProperty("spring.application.name");

        System.out.println("path = " + logPath + ", name = " +logName);
        Context context = getContext();
        context.putProperty("logPath", logPath);
        context.putProperty("logName", logName);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }
}
