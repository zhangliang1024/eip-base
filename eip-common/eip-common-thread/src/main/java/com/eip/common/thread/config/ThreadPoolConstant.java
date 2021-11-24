package com.eip.common.thread.config;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 动态线程池配置
 */
public class ThreadPoolConstant {

    //Nacos默认线程池配置名称
    public static final String HREAD_POOL = "thread-pool.properties";
    //Nacos默认组
    public static final String BIZ_GROUP = "DEFAULT_GROUP";

    //配置文件格式
    public static final String PROPERTIES_YAML = "yaml";
    public static final String PROPERTIES_JSON = "json";
    public static final String PROPERTIES_XML = "xml";
    public static final String PROPERTIES_PROPERTIES = "properties";
    public static final String PROPERTIES_YML = "yml";


    //默认等待刷新时间
    public static final int DEFAULT_NACOS_WAIT_REFRESH_CONFIG_SECONDS = 1;
    //默认等待配置刷新
    public static final boolean DEFAULT_IS_WAIT_REFRESH_CONFIG = true;
    //默认核心线程数
    public static final int DEFAULT_CORE_POOL_SIZE = 1;
    //默认告警间隔时间
    public static final int DEFAULT_ALARM_TIME_INTERVAL = 1;
    //默认队列大小
    public static final int DEFAULT_QUEUE_CAPACITY_SIZE = Integer.MAX_VALUE;
    //默认线程池名称
    public static final String DEFAULT_THREAD_POOL_NAME = "eip-pool";
}
