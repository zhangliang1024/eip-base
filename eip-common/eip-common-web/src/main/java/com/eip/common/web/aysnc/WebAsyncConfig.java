package com.eip.common.web.aysnc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 1.当使用@EnableAsync情况下的异步处理时
 * 2.当在项目中使用@Async处理移除任务时，使用这里定义的线程池代替springboot默认提供来进行处理
 */
public class WebAsyncConfig {

    @Autowired
    private AsyncProperties asyncProperties;

    @Bean
    @Primary
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数 推荐设置为 CPU核心数
        executor.setCorePoolSize(asyncProperties.getCoreNum());
        //线程数最大值 推荐设置为CPU核心数 * 2
        executor.setMaxPoolSize(asyncProperties.getMaxNum());
        //线程的空闲时间，超过maxPoolSize的线程，达到空闲时间后会被回收
        executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
        //是否允许回收核心线程
        executor.setAllowCoreThreadTimeOut(asyncProperties.getSetAllowCoreThreadTimeOut());
        //设置拒绝策略，选择采用当前线程执行被拒绝的任务
        executor.setRejectedExecutionHandler(asyncProperties.getRejectHandler().getRejectedExecutionHandler());
        //设置线程名称的前缀
        executor.setThreadNamePrefix(asyncProperties.getPrefixName());
        //设置阻塞队列大小
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        //初始化线程池
        executor.initialize();
        return executor;
    }
}
