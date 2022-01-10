package com.eip.common.web.aysnc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Data
@ConfigurationProperties(prefix = "eip.async.thread.pool")
public class AsyncProperties {

    /**
     * 线程的前缀名称
     */
    private String prefixName = "eip-executor-";
    /**
     * 核心线程数
     */
    private Integer coreNum;
    /**
     * 最大线程数
     */
    private Integer maxNum;
    /**
     * 线程的最大空闲时间
     */
    private Integer keepAliveSeconds = 60;
    /**
     * 时候允许核心线程数超时
     */
    private Boolean setAllowCoreThreadTimeOut = false;
    /**
     * 设置阻塞队列大小
     */
    private Integer queueCapacity = 1000;
    /**
     * 拒绝策略 - 默认自行处理任务
     */
    private RejectEnum rejectHandler = RejectEnum.CallerRunsPolicy;

    @PostConstruct
    public void init() {
        //获取CPU核心数
        Runtime runtime = Runtime.getRuntime();
        int cpuCore = runtime.availableProcessors();

        //如果没有设置核心线程数，则直接设置cpu的核心数
        if (coreNum == null) {
            coreNum = cpuCore;
        }

        //最大核心数为cpu核心数 * 2
        if (maxNum == null) {
            maxNum = cpuCore * 2;
        }
    }

    public enum RejectEnum {
        /**
         * 抛出异常
         */
        AbortPolicy(new ThreadPoolExecutor.AbortPolicy()),
        /**
         * 提交任务的线程自行处理
         */
        CallerRunsPolicy(new ThreadPoolExecutor.CallerRunsPolicy()),
        /**
         * 线程池会放弃等待队列中最旧的未处理任务，然后将被拒绝的任务添加到等待队列中
         */
        DiscardOldestPolicy(new ThreadPoolExecutor.DiscardOldestPolicy()),
        /**
         * 直接丢弃被拒绝的任务
         */
        DiscardPolicy(new ThreadPoolExecutor.DiscardPolicy());

        /**
         * 实际的拒绝策略
         */
        private RejectedExecutionHandler rejectedExecutionHandler;

        /**
         * 构造方法
         *
         * @param rejectedExecutionHandler
         */
        RejectEnum(RejectedExecutionHandler rejectedExecutionHandler) {
            this.rejectedExecutionHandler = rejectedExecutionHandler;
        }

        public RejectedExecutionHandler getRejectedExecutionHandler() {
            return rejectedExecutionHandler;
        }
    }
}
