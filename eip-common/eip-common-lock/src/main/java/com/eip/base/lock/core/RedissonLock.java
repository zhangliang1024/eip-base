package com.eip.base.lock.core;

import com.eip.base.lock.eums.TransactionMode;
import com.eip.base.lock.transaction.TransactionManager;
import com.eip.base.lock.utils.RLockThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 11:06
 * @Version: V1.0.0
 * @Description: 对Redisson获取分布式锁扩展
 */
@Slf4j
public class RedissonLock {

    private Redisson redisson;
    private RedissonManager redissonManager;

    public RedissonLock(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
        this.redisson = redissonManager.getRedisson();
    }

    public RedissonManager getRedissonManager() {
        return redissonManager;
    }

    public void setRedissonManager(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
    }

    /**
     * redis上锁 - 没有获得锁该方法会立刻返回null
     */
    public boolean tryLock(String key) {
        log.debug("[lock] - 尝试分布式锁 - {}", key);
        RLock lock = redisson.getLock(key);
        //尝试获得分布式锁
        if (lock != null && lock.tryLock()) {
            log.debug("[lock] - 获得分布式锁成功 - {}", key);
            RLockThreadLocal.setRLock(lock);
            return true;
        }
        log.warn("[lock] - 获取分布式锁失败 - {}", key);
        return false;
    }

    /**
     * redis上锁 - 限定重试次数
     *
     * @param key  锁名称
     * @param leaseTime 锁有效时间
     * @param waitTime  等待时间
     * @param tryCount  重试次数
     */
    public boolean tryLock(String key, long leaseTime, long waitTime, int tryCount) {
        log.debug("[lock] - 指定次数式获取分布式锁 - {} - {}", key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        RLock lock = redisson.getLock(key);
        boolean isLock = false;
        int num = 0;
        if (num < tryCount && !isLock) {
            num++;
            try {
                if(lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)){
                    log.debug("[lock] - 指定次数式获取分布式锁成功 - {} - {}", key,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    RLockThreadLocal.setRLock(lock);
                    isLock = true;
                    return isLock;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.warn("[lock] - 指定次数式获取分布式锁失败 - {} - {}", key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return isLock;
    }



    public boolean lockSync(String key) {
        log.debug("[lock] - 阻塞式获取分布式锁 - {} - {}", key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        RLock lock = redisson.getLock(key);
        if (lock != null) {
            while (!lock.tryLock()) {
                log.debug("[lock] - 阻塞式获取分布式锁成功 - {} - {}", key,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                RLockThreadLocal.setRLock(lock);
                return true;
            }
        }
        log.warn("[lock] - 阻塞式获取分布式锁失败 - {} - {}", key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        throw new RuntimeException("获得分布式锁异常！");
    }


    /**
     * 判断该线程是否持有当前锁
     * 如果该线程还持有该锁，那么释放该锁。
     * 如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
     */
    public boolean isHoldLock(String key) {
        RLock lock = redisson.getLock(key);
        if (lock != null) {
            return lock.isHeldByCurrentThread();
        }
        return false;
    }

    /**
     * redis上锁 - 超过指定时间没获取锁就返回null
     */
    public boolean lockTime(String key, Long time, TimeUnit unit) {
        log.debug("[lock] - 超时式获取分布式锁 - {} - {}", key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        RLock lock = redisson.getLock(key);
        try {
            if (lock.tryLock(time, unit)) {
                log.debug("[lock] - 超时式获取分布式锁成功 - {} - {}", key,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                RLockThreadLocal.setRLock(lock);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.warn("[lock] - 超时式获取分布式锁失败 - {} - {}", key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return false;
    }

    /**
     * 解锁方法 - 用于AOP解锁
     *
     * @return
     */
    public boolean unlock() {
        log.debug("[lock] - 解除分布式锁");
        //将上锁对象放入ThreadLocal中
        RLock rLock = RLockThreadLocal.getRLock();
        //清空ThreadLocal
        RLockThreadLocal.clear();
        if (rLock != null && rLock.isLocked()) {
            //解除分布式锁
            rLock.unlock();
            log.debug("[lock] - 解除分布式成功- {}", rLock.getName());
            return true;
        }
        log.warn("[lock] - 解除分布式失败- {}", rLock.getName());
        return false;
    }


    /**
     * 添加事务锁
     */
    public LockTransaction lockTransactiion(String key) {
        //添加分布式锁
        lockSync(key);
        return new LockTransaction();
    }


    /**
     * 事务锁
     */
    public static class LockTransaction {

        /**
         * 设置事务模式
         */
        public LockTransaction mode(TransactionMode mode) {
            //开启事务
            TransactionManager.startTransaction(mode);
            return this;
        }

        /**
         * 执行相应方法
         */
        public <T> T exec(Supplier<T> supplier) {
            try {
                T result = supplier.get();
                //提交事务
                TransactionManager.commit();
                return result;
            } catch (Exception e) {
                //回滚事务
                TransactionManager.rollback();
                throw e;
            } finally {
                //解除分布式锁

            }
        }
    }
}
