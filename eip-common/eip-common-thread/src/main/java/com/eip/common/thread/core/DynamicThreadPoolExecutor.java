package com.eip.common.thread.core;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 自定义ThreadPoolExecutor
 */
public class DynamicThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * 线程池名称
     */
    private String threadPoolName;
    /**
     * The default rejected execution advice
     */
    private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();


    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, defaultHandler);
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                     RejectedExecutionHandler handler, String threadPoolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.threadPoolName = threadPoolName;
    }


    //最短执行时间
    private long minCostTime;
    //最长执行时间
    private long maxCostTime;
    //总的耗时
    private AtomicLong totalCostTime = new AtomicLong();

    private ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        startTimeThreadLocal.set(System.currentTimeMillis());
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        long costTime = System.currentTimeMillis() - startTimeThreadLocal.get();
        startTimeThreadLocal.remove();
        maxCostTime = maxCostTime > costTime ? maxCostTime : costTime;
        if (getCompletedTaskCount() == 0) {
            minCostTime = costTime;
        }
        minCostTime = minCostTime < costTime ? minCostTime : costTime;
        totalCostTime.addAndGet(costTime);
        super.afterExecute(r, t);
    }

    public long getMinCostTime() {
        return minCostTime;
    }

    public long getMaxCostTime() {
        return maxCostTime;
    }

    public long getAverageCostTime() {//平均耗时
        if (getCompletedTaskCount() == 0 || totalCostTime.get() == 0) {
            return 0;
        }
        return totalCostTime.get() / getCompletedTaskCount();
    }
}
