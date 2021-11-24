package com.eip.base.lock.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.TransactionStatus;

/**
 * 事务开启标识
 */
@Slf4j
public class TransactionThreadLocal {

    public static ThreadLocal<TransactionStatus> transactionStatusThreadLocal = new ThreadLocal<>();

    /**
     * 获取事务状态对象
     * @return
     */
    public static TransactionStatus getTransactionStatusThreadLocal() {
        return transactionStatusThreadLocal.get();
    }

    /**
     * 设置事务状态对象
     * @param transactionStatus
     */
    public static void setTransactionStatusThreadLocal(TransactionStatus transactionStatus) {
        TransactionThreadLocal.transactionStatusThreadLocal.set(transactionStatus);
    }

    /**
     * 清空
     */
    public static void clear(){
        log.debug("[Transaction-Clear] - 清空事务状态...");
        transactionStatusThreadLocal.remove();
    }
}
