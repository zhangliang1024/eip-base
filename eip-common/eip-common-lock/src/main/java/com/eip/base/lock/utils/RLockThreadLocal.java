package com.eip.base.lock.utils;

import org.redisson.api.RLock;

/**
 * 暂存RLock对象的工具类
 */
public class RLockThreadLocal {

    private static ThreadLocal<RLock> rLockThread = new ThreadLocal<>();

    public static void setRLock(RLock rLock){
        //设置锁标识
        rLockThread.set(rLock);
    }

    public static RLock getRLock(){
        return rLockThread.get();
    }

    public static void clear(){
        //清空锁表示
        rLockThread.remove();
    }
}
