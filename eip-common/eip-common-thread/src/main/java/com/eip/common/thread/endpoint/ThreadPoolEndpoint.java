package com.eip.common.thread.endpoint;

import com.eip.common.thread.config.DynamicThreadPoolProperties;
import com.eip.common.thread.core.DynamicThreadPoolExecutor;
import com.eip.common.thread.core.DynamicThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 线程池端点信息
 */
@Endpoint(id = "thread-pool")
public class ThreadPoolEndpoint {

    @Autowired
    private DynamicThreadPoolManager dynamicThreadPoolManager;

    @Autowired
    private DynamicThreadPoolProperties dynamicThreadPoolProperties;

    @ReadOperation
    public Map<String, Object> threadPoolMetrics() {

        Map<String, Object> data = new HashMap<>();
        List<Map> threadPools = new ArrayList<>();

        dynamicThreadPoolProperties.getExecutors().forEach(prop -> {
            DynamicThreadPoolExecutor executor = dynamicThreadPoolManager.getThreadPoolExecutor(prop.getThreadPoolName());
            AtomicLong rejectCount = dynamicThreadPoolManager.getRejectCount(prop.getThreadPoolName());
            Map<String, Object> pool = new HashMap<>();

            pool.put("thread.pool.name", prop.getThreadPoolName()); //线程名称

            //线程相关
            pool.put("thread.pool.thread.active.count", executor.getActiveCount()); //活跃线程数
            pool.put("thread.pool.thread.count", executor.getPoolSize()); //获取线程池中当前的线程数
            pool.put("thread.pool.thread.core.size", executor.getCorePoolSize()); //核心线程数
            pool.put("thread.pool.thread.max.size", executor.getMaximumPoolSize()); //最大线程池
            pool.put("thread.pool.thread.largest.size", executor.getLargestPoolSize()); //线程池历史最大的线程数

            //队列相关
            pool.put("thread.pool.queue.name", executor.getQueue().getClass().getSimpleName()); //使用队列名称
            pool.put("thread.pool.queue.count", executor.getQueue().size()); //当前排队任务数
            pool.put("thread.pool.queue.remaining.count", executor.getQueue().remainingCapacity()); //队列剩余大小
            pool.put("thread.pool.queue.completed.taskCount", executor.getCompletedTaskCount()); //返回线程池总共完成过的任务数
            pool.put("thread.pool.queue.task.count", executor.getTaskCount()); //返回线程池总共执行过的任务数（包括完成的、正在执行的，以及还在队列中的）

            //拒绝策略相关
            pool.put("thread.pool.rejected.name", executor.getRejectedExecutionHandler().getClass().getSimpleName()); //饱和策略名称
            pool.put("thread.pool.reject.count", rejectCount == null ? 0 : rejectCount.get()); //被拒绝总数

            //耗时相关
            pool.put("thread.pool.costtime.min", executor.getMinCostTime()); //最短执行时间
            pool.put("thread.pool.costtime.average", executor.getAverageCostTime()); //平均耗时
            pool.put("thread.pool.costtime.max", executor.getMaxCostTime()); //最长执行时间

            //统计相关
            pool.put("thread.pool.thread.activity", divide(executor.getActiveCount(), executor.getMaximumPoolSize())); //线程池活跃度
            pool.put("thread.pool.queue.useage", divide(executor.getQueue().size(),executor.getQueue().size() + executor.getQueue().remainingCapacity())); //队列使用度

            threadPools.add(pool);
        });
        data.put("threadPools", threadPools);
        return data;
    }

    /**
     * 保留两位小数
     */
    private static String divide(int num1, int num2) {
        return String.format("%1.2f%%", Double.parseDouble(num1 + "") / Double.parseDouble(num2 + "") * 100);
    }

}
