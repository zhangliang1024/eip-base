# 基于redisson实现的分布式锁
> 支持注解@Lock方式和常规加锁方式。使用策略+工厂模式慢则不同环境的部署

[演示demo](https://github.com/zhangliang1024/spring-fox-spring-boot-sample)

### 一、分布式锁的条件
> 分布式锁要满足的一些条件
```markdown
1. 互斥：在分布式高并发环境下，要保证同一时刻只能有一个线程获得锁。
2. 防止死锁：防止因为系统宕机或故障导致锁无法释放引起的死锁问题，所以锁必须设置过期时间。
3. 高性能：对于访问量大的共享资源，要减少锁的等待时间，避免线程阻塞。
   a. 锁的颗粒度要尽可能的小
   b. 锁的范围要尽量小
4. 重入：同一个线程可以重复拿到同一个锁。
```

### 二、使用方式
- 加锁方式
> 注解方式
```java
@Lock(value="goods", leaseTime=5)
public String lockDecreaseStock() {
    //业务逻辑
}
```
> 常规方式
```java
redissonLock.CLock("CLock", 10L);
    //业务逻辑
if (redissonLock.isHeldByCurrentThread("CLock")) {
    redissonLock.unlock("CLock");
}
```

- 部署方式
> 支持 standalone sentinel cluster masterslave 四种方式，通过配置文件指定
```yaml
redisson:
  CLock:
    server:
      address: 127.0.0.1:6379
      type: standalone
      password: 123456
      database: 0
```

- 推荐使用方式
> 常规加锁。注解方式加锁为整个方法，颗粒度太大。

### 三、`Redis`集群分布式锁问题
> - 高可用方案中主从部署方式，会存在主节点写入成功，但未同步到从节点，此时主节点宕机。从节点立刻被提升为主节点，其它线程又可以加锁了
> - Redis官方提出了一种 RedLock 的算法，Redisson 已经实现 RedLock 算法。


### 四、`RedisLock`简单实现
> 基于`SpringBoot Redis Data`
- `pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <exclusions>
        <exclusion>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
        </exclusion>
        <exclusion>
            <groupId>io.lettuce</groupId>
            <artifactId>lettuce-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
<groupId>redis.clients</groupId>
<artifactId>jedis</artifactId>
</dependency> 
```
- `代码实现`
```java
package ins.wacp.auctionActivity.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: RedisLock
 * Function: 使用RedisTemplate+Lua脚本实现Redis分布式锁
 * <p>
 * 使用分布式锁需要考虑的几点要求：
 * 1. 互斥性：在任意时刻，只能有一个线程能够获得锁
 * 2. 不会死锁：一个线程获得锁后，不会一直持有不释放，导致其它线程无法获取锁
 * 3. 加解锁是同一线程：解锁线程必须是加锁的线程，防止锁被其它线程释放
 * 4. 可重入性：同一线程在持有锁的情况下可以再次获取锁
 * 5. 健壮性：
 * </p>
 * Date: 2022年02月25 16:51:56
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class RedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Long RELEASE_SUCCESS = 1L;

    private static final long DEFAULT_TIMEOUT = 1000 * 10;

    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 尝试获取锁 立即返回
     *
     * @param lockKey
     * @param lockValue
     * @param expireTime
     */
    public boolean lock(String lockKey, String lockValue, long expireTime) {
        String currentValue = redisTemplate.opsForValue().get(lockKey);
        if (StringUtils.isNotBlank(currentValue) && currentValue.equalsIgnoreCase(lockValue)) {
            return true;
        }
        return redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 以阻塞方式的获取锁
     *
     * @param lockKey
     * @param lockValue
     * @param expireTime
     * @return
     */
    public boolean lockBlock(String lockKey, String lockValue, long expireTime) {
        long start = System.currentTimeMillis();

        // 检测是否超时
        while (System.currentTimeMillis() - start <= expireTime) {
            // 执行set命令
            // setIfAbsent支持原子操作
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            if (StringUtils.isNotBlank(currentValue) && currentValue.equalsIgnoreCase(lockValue)) {
                return true;
            }
            Boolean absent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.MILLISECONDS);
            // 其实没必要判NULL，这里是为了程序的严谨而加的逻辑 , 是否成功获取锁
            if (absent != null && absent) {
                return true;
            }
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey   锁的key
     * @param lockValue 请求标识
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String lockValue) {
        // 使用Lua脚本：先判断是否是自己设置的锁，再执行删除
        // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
        // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常EvalSha is not supported in cluster environment.，所以只能拿到原redis的connection来执行脚本
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
                }
                // 单机模式
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
                }
                return 0L;
            }
        });

        // 返回最终结果
        return RELEASE_SUCCESS.equals(result);
    }

    /**
     * 获取锁
     *
     * @param lockKey       锁的key
     * @param lockValue     请求标识
     * @param expireTime    超期时间
     * @param retryTimes    重试次数
     * @param retryInterval 重试间隔
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String lockValue, int expireTime, int retryTimes, int retryInterval) {
        // 重试次数
        int times = 0;
        while (times < retryTimes) {
            // 获取锁
            if (lock(lockKey, lockValue, expireTime)) {
                return true;
            }
            // 重试间隔
            retryInterval(retryInterval);
            times++;
        }
        return false;
    }

    private static void retryInterval(int retryInterval) {
        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取锁（加入自动锁自动延时）
     *
     * @param lockKey       锁的key
     * @param lockValue     请求标识
     * @param expireTime    超期时间
     * @param retryTimes    重试次数
     * @param retryInterval 重试间隔
     * @param autoDelayTime 自动延时时间
     * @return 是否获取成功
     */
    public boolean tryLockWithAutoDelay(String lockKey, String lockValue, int expireTime, int retryTimes, int retryInterval, int autoDelayTime) {
        // 重试次数
        int times = 0;
        while (times < retryTimes) {
            // 获取锁
            if (lock(lockKey, lockValue, expireTime)) {
                // 启动自动延时线程
                new Thread(() -> {
                    while (true) {
                        try {
                            // 自动延时
                            redisTemplate.expire(lockKey, expireTime + autoDelayTime, TimeUnit.MILLISECONDS);
                            // 休眠
                            TimeUnit.MILLISECONDS.sleep(autoDelayTime / 2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            }
            // 重试间隔
            retryInterval(retryInterval);
            times++;
        }
        return false;
    }
}
```

### 五、参考文档
[spring-boot-distributed-redisson](https://github.com/yudiandemingzi/spring-boot-distributed-redisson)

[Redisson实现分布式锁](https://www.cnblogs.com/qdhxhz/p/11046905.html)

[可重入分布式锁，终于完美的实现了](https://juejin.cn/post/7079695284099350559)