# Distributed Limit 
> 基于Guava RateLimiter令牌桶算法实现 和 Reids实现的分布式限流组件。本地使用RateLimiter，分布式环境使用Redis

---

### 一、说明：
```markdown
1. 保护高并发系统的三把利器：缓存、降级、限流。限流在很多场景中被用来控制并发和请求量，保护自身和下游系统不被流量冲垮。典型如：秒杀系统
2. 限流的目的：通过对并发访问\请求进行限速或者一个时间窗口内的请求进行限速来保护系统，一旦达到限制速率则拒绝服务或进行流量整形。
3. 常用的限流方式和场景：
   - 限制总并发数(如：数据库连接池、线程池)
   - 限制瞬时并发(如：nginx的limit_conn模块，限制瞬时并发连接。Java的Semaphore信号量)
   - 限制时间窗口的平均速率(如：nginx的limit_req模块，限制每秒平均速率。 Guava的RateLimiter)
场景：
   - 如限制方法(同一时间)被调用的并发数不能超过100，可以使用信号量Semaphore实现
   - 如限制方法(在一段时间内)平均被调用的次数不超过100，则可以使用RateLimiter  
4. 从架构维度考虑限流设计：
   - 在真实的大型项目中，不会只使用一种限流手段，往往是几种方式互相搭配使用。让限流有一种层次感，达到资源最大使用率。
   - 限流策略在设计上要上宽下紧(网关->服务->缓存->数据库)，不同部位的限流方案要尽量关注当前组件的高可用。    
```

### 二、使用
> pom.xml
```xml
<dependency>
    <groupId>com.zhliang.pzy</groupId>
    <artifactId>distributed-limit-spirng-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

> application.yaml
```yaml
server:
  port: 8090
  redis:
    host: localhost
    port: 6379
    database: 0
pzy:
  redis:
    limit:
      enabled: true
      # 指定类型：local redis,默认：local
      type: redis
```

> 测试接口
```java
@RestController
public class LimitController {

    @GetMapping("limit")
    @RedisLimit(prefix = "name",expire = 1,count = 10)
    public String limit(@RedisParam String name,@RedisParam int age){
        return "hello " + name;
    }
}
```

### 三、支持扩展限流类型
> 实现限流类型接口和具体的执行接口
```java
/**
 * 限流类型接口
 */
@Service
public interface LimitTypeService {
    String getType();
}

/**
 * 限流执行接口
 */
public interface LimitHandler {

    boolean tryAcquire(String key, long limitCount, String description, long expire, TimeUnit timeUnit);
}

```

> 通过注入自定义实现，代替默认实现
```java
@Bean
public LimitHandler limitHandler(LimitTypeService limitTypeService){
}

@Bean
public LimitTypeService limitTypeService(){
}
```

### SpringBoot+Tomcat修改线程数
```yaml
server:
  tomcat:
    # 等待队列长度 默认：100
    accept-count: 100
    # 最大工作线程 默认：200 (4核8g内存，线程数经验值800)
    max-threads: 200
    # 最大连接数 默认：8192
    max-connections: 8192
    # 最小工作空闲线程数 默认：10
    min-spare-threads: 10
```


### 四、常用算法
> 两桶两窗
#### 漏桶算法
> 漏桶作为计量工具时，可以用于流量塑形和流量控制
1. 漏桶算法有一个固定大小的桶，按照常量固定的速率流出
2. 如果桶是空的，请求将被限流。请求可以以任意速率流入水到桶中。如果流入超出桶的容量，发生溢出现象则丢弃请求
3. 桶容量是不变的

> 漏桶算法有2个变量：一个是桶的大小、一个是桶的流出速率

> 优缺点：

优点；按固定速率流出，平滑网络上的突发流量。网络中不会出现资源冲突
缺点：对于突发性流量来说缺乏效率，不能及时快速流出

#### 令牌桶算法
> 令牌桶算法: 是一个存放固定容量令牌的桶，按照固定速率往桶里添加令牌。有请求过来从令牌桶中拿令牌去执行请求
1. 令牌桶算法有一个固定容量的桶，按照固定的速率往桶里添加令牌。
2. 如果桶是空的，请求将会被限流(丢弃或缓冲区等待)。如果桶已经满了，新添加的令牌将被丢弃。可以一次申请一个或多个令牌进行消费
3. 桶容量是不变的

> 令牌桶算法有2个变量：桶的大小、令牌的流入速率

> 优缺点：

优点；可以应对突发流量




### 五、参考文档
有代码示例：令牌桶、信号量(Semaphore)
[使用Guava的RateLimiter做限流 ](https://my.oschina.net/hanchao/blog/1833612)
[超详细的Guava RateLimiter限流原理解析](https://www.jianshu.com/p/362d261115e7)
[限流原理解读之guava中的RateLimiter](https://cloud.tencent.com/developer/article/1459168)

[从构建分布式秒杀系统聊聊限流特技](https://www.imooc.com/article/35362)



[优雅解决分布式限流](https://www.cnblogs.com/lywJ/p/10715367.html)
[spring-boot2-learning-27章](https://github.com/battcn/spring-boot2-learning)
