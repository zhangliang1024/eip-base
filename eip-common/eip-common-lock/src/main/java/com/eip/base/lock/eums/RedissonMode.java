package com.eip.base.lock.eums;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 11:06
 * @Version: V1.0.0
 * @Description: redis部署方式
 */
public enum RedissonMode {

    STANDALONE("STANDALONE"),
    SENTINEL("SENTINEL"),
    CLUSTER("CLUSTER"),
    MASTERSLAVE("MASTERSLAVE");

    private final String connection_type;

    RedissonMode(String connection_type) {
        this.connection_type = connection_type;
    }

    public String getConnection_type() {
        return connection_type;
    }

}
