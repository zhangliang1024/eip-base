package com.eip.common.thread.config;

import com.eip.common.thread.eums.QueueTypeEnum;
import com.eip.common.thread.eums.RejectedExecutionHandlerEnum;
import lombok.Data;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 线程池配置类
 */
@Data
public class ThreadPoolProperties {

    /**
     * 线程池名称
     */
    private String threadPoolName = ThreadPoolConstant.DEFAULT_THREAD_POOL_NAME;
    /**
     * 核心线程数
     */
    private int corePoolSize = ThreadPoolConstant.DEFAULT_CORE_POOL_SIZE;
    /**
     * 最大线程数:默认为CPU核心数
     */
    private int maximumPoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 队列最大数
     */
    private int queueCapacity = ThreadPoolConstant.DEFAULT_QUEUE_CAPACITY_SIZE;
    /**
     * 队列类型
     */
    private String queueType = QueueTypeEnum.LINKED_BLOCKING_QUEUE.getQueueType();
    /**
     * 拒绝策略
     */
    private String rejectedExecutionType = RejectedExecutionHandlerEnum.ABORT_POLICY.getRejectedType();
    /**
     * 空闲线程存活时间
     */
    private long keepAliveTime;
    /**
     * 空闲线程存活时间单位
     */
    private TimeUnit unit = TimeUnit.MILLISECONDS;
    /**
     * 队列容量阈值，超过此值预警
     */
    private int queueCapacityThreshold = queueCapacity;
    /**
     * SynchronousQueue 是否公平策略
     */
    private boolean fair;

}
