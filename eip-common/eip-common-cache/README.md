## JAVA二级缓存系统 基于Guava+Redis
> 在项目开发中，我们经常会用到“缓存”来提升系统性能。使用好缓存也是一个开发人员要具备的基本技能。

[演示Demo](https://github.com/zhangliang1024/spring-fox-spring-boot-sample/tree/master/src/main/java/com/zhliang/pzy/spring/fox/cache)

### 一、项目介绍
```markdown
1. 大型分布式系统缓存key设计：
   示例：{0}_area_{1}
   第0项 通常会用一个用于标识业务意义的字符串，如：命名空间、业务部门。不同业务部门可以自定义
   第1项 通常对应于标识数据库的唯一标识
   
   这样设计拼接的缓存key,可以降低不同业务部门不同开发人员命名相同缓存键的可能。这一点在一个业务部门众多而部署的分布式缓存系统只有一套的环境下显得尤为重要。
   
2. 及时更新“缓存”方案
   在使用分布式缓存后，必须要考虑如果业务发生变化，缓存数据与数据库一致的问题。而且大多时时候，缓存中的数据会涉及到很多业务联系。
   
   通过增加“缓存版本”来相对灵活的控制缓存
   a. 在自己的业务系统中新建一张表：cache_vseriosn ，只有一个字段：version，记录缓存版本。并初始化一条记录，且只有一条记录
   b. 将cache_version的数据写入分布式缓存，按自己的业务定义version对应的缓存键。如：namespace_area_areaid_version:v0
      对你所在的业务系统，这个缓存版本可以认为是个常量。
   c. 重新定义后的缓存key：{0}_area_{1}_{2} 。其中2代表的就是：缓存版本
   d. 若后台有业务数据发生变更，直接更新业务数据。不需要更新缓存
   e. 在一个独立管理模块中，如果业务需要更新缓存。则更新cache_version表中的version记录，并重置分布式缓存中的version值。
   
   缺点：
   a. 新增了数据表用于维护缓存版本：version
   b. 多了cache_version操作模块
   c. 每次更新缓存，依赖这个版本的缓存都会“被过期”。如存在并发访问，要注意缓存雪崩问题   
      
       
3. Guava支持的过期策略：
   a. FIFO  先进先出
   b. LRU   最近最少使用  
   c. LFU   最不经常使用
   
   
4. Guava对不同key过期时间的支持
   a. Guava Cache对缓存的过期时间设置不是很友好。
   b. 若缓存的key过期时间不同，Gauva通过实例化多份Cache实例
   c. 本项目实现了，根据不同的过期时间来生产多份Cache缓存实例     

```


### 二、使用说明
> pom
```xml
 <!--二级缓存-->
<dependency>
    <groupId>com.zhliang.pzy</groupId>
    <artifactId>cache-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
> java
```java
/**
 * 扫描二级缓存组件
 */
@Configuration
@ComponentScan(basePackages = "com.zhliang.pzy.cache")
public class CustomerCacheAutoConfig {

}

/**
 * 接口增加缓存
 * 1. 注解方式
 * 2. 自定义方式
 */
@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    PowerCacheBuilder cacheBuilder;

    @Cacheable(key = "uuid",expireTime = 6000L)
    @GetMapping("/uuid")
    public String getResult(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    @GetMapping
    public String cache(){

        //增加缓存版本
        version = cacheBuilder.resetCacheVersion();
        logger.info(String.format("重置后的缓存版本：%s", version));

        cacheKey = cacheBuilder.generateVerKey("goods_v1");
        cacheBuilder.set(cacheKey, "我的测试商品");
        String goodsVO2 = cacheBuilder.get(cacheKey);
        logger.info("增加缓存版本后 value : {}",goodsVO2);

        return "cache suceess";
    }
}
```
> application
```yaml
## 二级缓存Redis相关配置
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    password:
    # redis连接超时时间(默认：5分钟，单位：毫秒)
    timeout: 300000ms
    jedis:
      pool:
        # redis连接池最大连接数
        max-active: 512
        # redis连接池最小空闲连接
        min-idle: 0
        # redis连接池最大空闲连接
        max-idle: 8
        # redis连接池最大阻塞等待时间(-1表示没有限制)
        max-wait: -1ms


## 启用二级缓存配置
cache:
  power:
    # 启用本地缓存
    is-use-local-cache: local
    # 启用redis缓存
    is-use-redis-cache: redis
    # 本地缓存最大队列数
    cache-maximum-size: 10
    # 本地缓存分片(应对不同的过期时间，创建不同的缓存分片)
    cache-minute: 10
    # 缓存版本配置
    cache-version-key: version:v0
```


### 三、参考文档
> 

[贼厉害，手撸的 SpringBoot缓存系统，性能杠杠的！](https://blog.csdn.net/qq_17231297/article/details/108544347)

[【讨论帖】控制分布式缓存“及时”过期的一种实现](https://blog.csdn.net/weixin_34015336/article/details/90094421)

★★★

[jeffwongishandsome](https://www.cnblogs.com/jeffwongishandsome)



---

### 代码示例

```java
public abstract class MemoryCacheHandler<K extends Serializable, V> implements BaseCacheHandler<K, V> {


    public static Map<String, Cache<String, Object>> _cacheMap = Maps.newConcurrentMap();

    @Autowired
    private CacheProperties cacheProperties;

    static {

        Cache<String, Object> cacheContainer = CacheBuilder.newBuilder()
                .concurrencyLevel(CacheConstact.CACHE_CONCURRENCY_LEVEL)//并发级别，也就是可以同时操作的线程数
                .initialCapacity(11)//初始化容量
                .maximumSize(CacheConstact.CACHE_MAX_SIZE)//最大容量
                .expireAfterWrite(CacheConstact.DEFAULT_CACHE_SEONDS, TimeUnit.MILLISECONDS)//最后一次写入后的一段时间移出
                .expireAfterAccess(10, TimeUnit.MILLISECONDS) //最后一次访问后的一段时间移出
                .recordStats()//开启统计功能
                .build();

        _cacheMap.put(String.valueOf(CacheConstact.DEFAULT_CACHE_SEONDS), cacheContainer);
    }
}

```