package com.eip.common.thread.core;

import com.eip.common.thread.config.DynamicThreadPoolProperties;
import com.eip.common.thread.eums.QueueTypeEnum;
import com.eip.common.thread.eums.RejectedExecutionHandlerEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 动态线程池
 */
@Slf4j
public class DynamicThreadPoolManager {

    /**
     * 存储线程池对象，Key:名称 Value:对象
     */
    private Map<String, DynamicThreadPoolExecutor> threadPoolExecutorMap = new ConcurrentHashMap<>();
    /**
     * 存储线程池拒绝次数，Key:名称 Value:次数
     */
    private static Map<String, AtomicLong> threadPoolExecutorRejectCountMap = new ConcurrentHashMap<>();


    @Autowired
    private DynamicThreadPoolProperties threadPoolProperties;


    @PostConstruct
    public void init() {
        createThreadPoolExecutor(threadPoolProperties);
    }

    /**
     * 创建线程池
     */
    private void createThreadPoolExecutor(DynamicThreadPoolProperties threadPoolProperties) {
        threadPoolProperties.getExecutors().forEach(poolProperties -> {
            if (!threadPoolExecutorMap.containsKey(poolProperties.getThreadPoolName())) {
                DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(
                        poolProperties.getCorePoolSize(),
                        poolProperties.getMaximumPoolSize(),
                        poolProperties.getKeepAliveTime(),
                        poolProperties.getUnit(),
                        getBlockingQueue(poolProperties.getQueueType(), poolProperties.getQueueCapacity(),
                                poolProperties.isFair()),
                        new DynamicThreadFactory(poolProperties.getThreadPoolName()),
                        getRejectedExecutionHandler(poolProperties.getRejectedExecutionType(),
                                poolProperties.getThreadPoolName()),
                        poolProperties.getThreadPoolName()
                );
                threadPoolExecutorMap.put(poolProperties.getThreadPoolName(), threadPoolExecutor);
            }
        });
        log.info("[dynamic pool] - {} num thread-pool init success ", threadPoolProperties.getExecutors().size());
    }

    /**
     * 拒绝策略
     */
    private RejectedExecutionHandler getRejectedExecutionHandler(String rejectedExecutionType, String threadPoolName) {
        if (RejectedExecutionHandlerEnum.CALLER_RUNS_POLICY.getRejectedType().equals(rejectedExecutionType)) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        }
        if (RejectedExecutionHandlerEnum.DISCARD_OLDEST_POLICY.getRejectedType().equals(rejectedExecutionType)) {
            return new ThreadPoolExecutor.DiscardOldestPolicy();
        }
        if (RejectedExecutionHandlerEnum.DISCARD_POLICY.getRejectedType().equals(rejectedExecutionType)) {
            return new ThreadPoolExecutor.DiscardPolicy();
        }
        ServiceLoader<RejectedExecutionHandler> serviceLoader = ServiceLoader.load(RejectedExecutionHandler.class);
        Iterator<RejectedExecutionHandler> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            RejectedExecutionHandler rejectedExecutionHandler = iterator.next();
            String rejectedExecutionHandlerName = rejectedExecutionHandler.getClass().getSimpleName();
            if (rejectedExecutionType.equals(rejectedExecutionHandlerName)) {
                return rejectedExecutionHandler;
            }
        }
        return new DynamicAbortPolicy(threadPoolName);
    }

    /**
     * 获取阻塞队列
     */
    private BlockingQueue getBlockingQueue(String queueType, int queueCapacity, boolean fair) {
        if (!QueueTypeEnum.exists(queueType)) {
            throw new RuntimeException("[dynamic pool] - : " + queueType + " queue is not exist");
        }
        if (QueueTypeEnum.ARRAY_BLOCKING_QUEUE.getQueueType().equals(queueType)) {
            return new ArrayBlockingQueue(queueCapacity);
        }
        if (QueueTypeEnum.SYNCHRONOUS_QUEUE.getQueueType().equals(queueType)) {
            return new SynchronousQueue(fair);
        }
        if (QueueTypeEnum.PRIORITY_BLOCKING_QUEUE.getQueueType().equals(queueType)) {
            return new PriorityBlockingQueue(queueCapacity);
        }
        if (QueueTypeEnum.DELAY_QUEUE.getQueueType().equals(queueType)) {
            return new DelayQueue();
        }
        if (QueueTypeEnum.LINKED_BLOCKING_DEQUE.getQueueType().equals(queueType)) {
            return new LinkedBlockingDeque(queueCapacity);
        }
        if (QueueTypeEnum.LINKED_TRANSFER_DEQUE.getQueueType().equals(queueType)) {
            return new LinkedTransferQueue();
        }
        return new ResizableCapacityLinkedBlockIngQueue(queueCapacity);
    }

    /**
     * 刷新线程池
     */
    public void refreshThreadPoolExecutor() {
        threadPoolProperties.getExecutors().forEach(poolProperties -> {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(poolProperties.getThreadPoolName());
            if (threadPoolExecutor != null) {
                threadPoolExecutor.setCorePoolSize(poolProperties.getCorePoolSize());
                threadPoolExecutor.setMaximumPoolSize(poolProperties.getMaximumPoolSize());
                threadPoolExecutor.setKeepAliveTime(poolProperties.getKeepAliveTime(), poolProperties.getUnit());
                threadPoolExecutor.setRejectedExecutionHandler(getRejectedExecutionHandler(poolProperties.getRejectedExecutionType(), poolProperties.getThreadPoolName()));
                BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
                if (queue instanceof ResizableCapacityLinkedBlockIngQueue) {
                    ((ResizableCapacityLinkedBlockIngQueue<Runnable>) queue).setCapacity(poolProperties.getQueueCapacity());
                }
            } else {
                createThreadPoolExecutor(threadPoolProperties);
            }
        });
    }

    public DynamicThreadPoolExecutor getThreadPoolExecutor(String threadPoolName) {
        DynamicThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (threadPoolExecutor == null) {
            throw new RuntimeException("[dynamic pool] - : " + threadPoolName + " thread pool is not exist");
        }
        return threadPoolExecutor;
    }

    public AtomicLong getRejectCount(String threadPoolName) {
        return threadPoolExecutorRejectCountMap.get(threadPoolName);
    }

    public void clearRejectCount(String threadPoolName) {
        threadPoolExecutorRejectCountMap.remove(threadPoolName);
    }


    /**
     * 自定义线程工厂
     */
    static class DynamicThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DynamicThreadFactory(String threadPoolName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = threadPoolName + "-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


    /**
     * 自定义饱和策略
     */
    static class DynamicAbortPolicy implements RejectedExecutionHandler {

        private String threadPoolName;

        public DynamicAbortPolicy() {
        }

        public DynamicAbortPolicy(String threadPoolName) {
            this.threadPoolName = threadPoolName;
        }

        /**
         * Always throws RejectedExecutionException.
         * 总是抛出异常
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            AtomicLong atomicLong = threadPoolExecutorRejectCountMap.putIfAbsent(threadPoolName, new AtomicLong(1));
            if (atomicLong != null) {
                atomicLong.incrementAndGet();
            }
            throw new RejectedExecutionException("[dynamic pool] - task : " + r.toString() + " , rejected from : [" + e.toString() + "]");
        }
    }

}
