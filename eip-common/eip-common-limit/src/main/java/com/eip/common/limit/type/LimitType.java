package com.eip.common.limit.type;

/**
 * 支持类型枚举
 */
public enum LimitType {

    LOCAL("local"),
    REDIS("redis")
    ;
    private String name;

    LimitType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
