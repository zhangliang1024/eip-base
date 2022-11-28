# 动态线程池插件

> - 插件支持通过开源`Nacos\Apollo`等配置中心，动态更新线程池参数。
> - 提供`Endpoint`端点`thread-pool`，可通过`/actuator/thread-pool`获取线程池运行指标信息，进行监控预警等操作


### 一、使用方式
> 这里以`apollo`为例
- `pom.xml`
```xml
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-client</artifactId>
    <version>1.7.0</version>
</dependency>
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-core</artifactId>
    <version>1.7.0</version>
</dependency>
<dependency>
    <groupId>com.eip.cloud</groupId>
    <artifactId>dynamic-thread-pool-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
- `apollo`配置示例
```properties
#order-pool 订单业务线程池
eip.dynamic.thread.pool.executors[0].corePoolSize = 5
eip.dynamic.thread.pool.executors[0].maximumPoolSize = 20
eip.dynamic.thread.pool.executors[0].keepAliveTime = 300
eip.dynamic.thread.pool.executors[0].threadPoolName = order-pool
eip.dynamic.thread.pool.executors[0].queueCapacity = 10000
eip.dynamic.thread.pool.executors[0].rejectedExecutionType = AbortPolicy

#pay-pool 交易业务线程池
eip.dynamic.thread.pool.executors[1].corePoolSize = 3
eip.dynamic.thread.pool.executors[1].maximumPoolSize = 19
eip.dynamic.thread.pool.executors[1].keepAliveTime = 300
eip.dynamic.thread.pool.executors[1].threadPoolName = pay-pool
eip.dynamic.thread.pool.executors[1].queueCapacity = 10000

```
- 启动来类加入注解`@EnableDynamicThreadPool`
```java
@EnableDynamicThreadPool
@EnableApolloConfig
@SpringBootApplication
public class ThreadPoolApplication {
}
```
- 通过`DynamicThreadPoolManager`获取线程池
```java
@Slf4j
@RestController
public class TestController {

    @Autowired
    private DynamicThreadPoolManager dynamicThreadPoolManager;

    @GetMapping("/execute/{threadPoolName}")
    public String doExecute1(@PathVariable("threadPoolName") String threadPoolName){
        DynamicThreadPoolExecutor threadPoolExecutor = dynamicThreadPoolManager.getThreadPoolExecutor(threadPoolName);
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(()->{
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        return "success";
    }
}
```
- 访问示例
```text
http://localhost:8001/execute/order-pool
```

### 二、`Endpoint`端点`thread-pool`
```text
http://localhost:8001/actuator/thread-pool
```
- 指标示例
```json
{
    "threadPools": [
        {
            "thread.pool.queue.name": "ResizableCapacityLinkedBlockIngQueue",
            "thread.pool.queue.completed.taskCount": 200,
            "thread.pool.costtime.average": 519,
            "thread.pool.thread.active.count": 0,
            "thread.pool.queue.remaining.count": 100,
            "thread.pool.queue.count": 0,
            "thread.pool.queue.task.count": 200,
            "thread.pool.thread.largest.size": 1,
            "thread.pool.costtime.min": 4,
            "thread.pool.name": "eip-pool",
            "thread.pool.thread.max.size": 10,
            "thread.pool.rejected.name": "DynamicAbortPolicy",
            "thread.pool.thread.count": 1,
            "thread.pool.queue.useage": "0.00%",
            "thread.pool.thread.core.size": 10,
            "thread.pool.thread.activity": "0.00%",
            "thread.pool.reject.count": 0,
            "thread.pool.costtime.max": 2738
        }
    ]
}
```
- 指标说明
```properties
#线程相关
thread.pool.name                 线程名称
thread.pool.thread.active.count  活跃线程数
thread.pool.thread.count         获取线程池中当前的线程数
thread.pool.thread.core.size     核心线程数
thread.pool.thread.max.size      最大线程池
thread.pool.thread.largest.size  线程池历史最大的线程数

#队列相关
thread.pool.queue.name             使用队列名称
thread.pool.queue.count            当前排队任务数
thread.pool.queue.remaining.count        队列剩余大小
thread.pool.queue.completed.taskCount    返回线程池总共完成过的任务数
thread.pool.queue.task.count             返回线程池总共执行过的任务数（包括完成的、正在执行的，以及还在队列中的）

#拒绝策略相关
thread.pool.rejected.name    饱和策略名称
thread.pool.reject.count     被拒绝总数

#耗时相关
thread.pool.costtime.min     最短执行时间
thread.pool.costtime.average 平均耗时
thread.pool.costtime.max     最长执行时间

#统计相关
thread.pool.thread.activity  线程池活跃度
thread.pool.queue.useage     队列使用度
```


### 三、配置说明
```properties
#监控告警
eip.dynamic.thread.pool.access-token = 钉钉机器人access_token
eip.dynamic.thread.pool.secret = 钉钉机器儿secret
eip.dynamic.thread.pool.alarm-api-url = 外部异常告警API地址
eip.dynamic.thread.pool.alarm-time-interval = 告警时间间隔，单位分钟
eip.dynamic.thread.pool.owner = 告警负责人

#配置监听
eip.dynamic.thread.pool.apollo-namespace = Apollo的namespace 监听配置变更使用
eip.dynamic.thread.pool.nacos-data-id = Nacos DataId 监听配置变更使用
eip.dynamic.thread.pool.nacos-group = Nacos Group 监听配置变更使用

#配置动态刷新
eip.dynamic.thread.pool.bool-wait-refresh-config = 是否配置等待刷新，默认为true。因为apollo、nacos配置变更后需要一定时间才能刷新到Spring容器中
eip.dynamic.thread.pool.wait-refresh-config-seconds = 配置等待刷新时间，单位秒

#线程池配置
eip.dynamic.thread.pool.executors[0].corePoolSize = 核心线程数
eip.dynamic.thread.pool.executors[0].maximumPoolSize = 最大线程数
eip.dynamic.thread.pool.executors[0].keepAliveTime = 空闲线程存活时间，单位毫秒
eip.dynamic.thread.pool.executors[0].threadPoolName = 线程池名称
eip.dynamic.thread.pool.executors[0].queueCapacity = 队列最大数
eip.dynamic.thread.pool.executors[0].queueType = 队列类型
eip.dynamic.thread.pool.executors[0].rejectedExecutionType = 拒绝策略 
eip.dynamic.thread.pool.executors[0].queueCapacityThreshold =队列容量阈值，超过此值预警
```
- 支持队列类型
```text
LinkedBlockingQueue
SynchronousQueue
ArrayBlockingQueue
DelayQueue
LinkedTransferQueue
LinkedBlockingDeque
PriorityBlockingQueue
```
- 支持拒绝策略
```text
CallerRunsPolicy
AbortPolicy
DiscardPolicy
DiscardOldestPolicy
```
- 支持动态调整队列的长度
```text
#将队列长度对外暴露可修改
ResizableCapacityLinkedBlockIngQueue 
```

### 四、告警说明
> 这里只提供钉钉告警配置，但没做实现。此插件还是围绕核心功能提供动态线程池的管理。
- 告警实现
```properties
1. 本插件暴露的thread-pool端点获取监控指标，通过Prometheus+Grafana组件做可视化监控和告警
2. 可自定义告警指标，通过DynamicThreadPoolProperties的getExecutors()获取线程池，然后计算告警指标进行告警
```
![](https://ae02.alicdn.com/kf/H3e7f057f928b48b9ba1ec52313856692F.png)

### 五、线程池监控
- 监控目的
> 监控是为了防患于未然，防止发生生产事故。

- 使用线程池可能出现的问题
```text
a. 线程池异步处理，消费速度过慢，导致任务积压，响应过慢。或者队列有限，导致拒绝提交。
b. 线程池做并行请求，请求量过大，导致处理积压，响应变慢。
c. 业务评估不准确，导致线程池资源设置不合理。
```

- 线程池监控的指标
```text
a. 队列的饱和度
b. 单位时间内提交任务的速度远大于消费速度
```

### 六、跨线程日志链路
> `MDC`底层使用`ThreadLocal`，无法传递跨线程的`TraceId`。对于大量跨线程的业务操作，形成日志链路，方便排查问题。

- 方案：封装线程池
> 在线程执行前将一个线程的日志`TraceId`传递给将要执行的线程日志`MDC`
```text
ThreadMDCUtil
```


### 七、参考文档

[基于SpringBoot集成线程池，实现线程的池的动态监控](https://www.cnblogs.com/mic112/archive/2021/10/19/15424574.html)
[跨线程(线程池)日志链路追踪](https://www.cnblogs.com/lyhc/p/16661650.html)
[自定义实现JAVA线程池的线程工厂类-ThreadFactory](https://www.jianshu.com/p/1411da2df689)
