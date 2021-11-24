package com.eip.base.lock.transaction;

import com.eip.base.lock.eums.TransactionMode;
import com.eip.base.lock.utils.ApplicationContextUtils;
import com.eip.base.lock.utils.TransactionThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 手动事务管理工具类
 */
@Slf4j
public class TransactionManager {

    private static DataSourceTransactionManager transactionManager;

    static {
        //事务管理对象
        transactionManager = ApplicationContextUtils.getBean(DataSourceTransactionManager.class);
    }


    /**
     * 开启事务 - 根据相应的事务模式
     *
     * @return
     */
    public static boolean startTransaction(TransactionMode mode) {
        //判断是否需要开启事务
        if (transactionManager != null) {
            log.debug("[Cluster-CLock] - 手动开启Spring事务....");
            //创建事务对象
            DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
            //选择线程模式
            if (mode == TransactionMode.NEW) {
                transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                //表示挂起当前事务，开启一个新的事务
            } else if (mode == TransactionMode.NOT) {
                transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
                //表示挂起当前事务，后续操作不使用任何事务
            }
            //开启事务
            TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
            //保存当前的事务状态对象
            TransactionThreadLocal.setTransactionStatusThreadLocal(transaction);
            return true;
        }
        return false;
    }

    /**
     * 提交事务
     *
     * @return
     */
    public static boolean commit() {
        if (transactionManager != null) {
            //获取当前的事务状态对象
            TransactionStatus transaction = TransactionThreadLocal.getTransactionStatusThreadLocal();
            if (transaction == null) {
                return false;
            }
            log.debug("[Cluster-CLock] - 提交Spring事务....");
            //提交当前事务
            transactionManager.commit(transaction);
            //清空事务状态
            TransactionThreadLocal.clear();
            return true;
        }
        return false;
    }

    /**
     * 回滚事务
     *
     * @return
     */
    public static boolean rollback() {
        if (transactionManager != null) {
            //获取当前的事务状态对象
            TransactionStatus transaction = TransactionThreadLocal.getTransactionStatusThreadLocal();
            if (transaction == null) {
                return false;
            }
            log.debug("[Cluster-CLock] - 回滚Spring事务....");
            //提交当前事务
            transactionManager.rollback(transaction);
            //清空事务状态
            TransactionThreadLocal.clear();
            return true;
        }
        return false;
    }
}
