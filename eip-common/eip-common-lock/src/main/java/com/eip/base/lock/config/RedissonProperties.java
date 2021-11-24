package com.eip.base.lock.config;

import com.eip.base.lock.eums.RedissonMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 11:06
 * @Version: V1.0.0
 * @Description: redis配置信息
 */
@Data
@ConfigurationProperties(prefix = "eip.lock.redisson.server")
public class RedissonProperties {

    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    private String address;
    /**
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    private String type = RedissonMode.STANDALONE.getConnection_type();
    /**
     * redis 连接密码
     */
    private String password;

    /**
     * 选取那个数据库
     */
    private int database;

}
