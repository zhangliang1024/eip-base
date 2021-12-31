package com.eip.common.datasource.context;

import com.eip.common.datasource.enums.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过ThreadLocal将数据源设置到每个线程上下文中
 */
@Slf4j
public class DBContextHolder {


    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void setContextHolder(DBTypeEnum dbType){
        contextHolder.set(dbType);
    }
    public static DBTypeEnum getContextHolder(){
        return contextHolder.get();
    }

    public static void master(){
        setContextHolder(DBTypeEnum.MASTER);
        log.info("Datasource is useing master ");
    }

    public static void slave(){
        //轮询
        int index = counter.getAndIncrement() % 2;
        if(counter.get() > 9999){
            counter.set(-1);
        }
        if(index == 0){
            setContextHolder(DBTypeEnum.SLAVE01);
            log.info("Datasource is useing slave01 ");
        }else {
            setContextHolder(DBTypeEnum.SLAVE02);
            log.info("Datasource is useing slave02 ");
        }
    }

}
