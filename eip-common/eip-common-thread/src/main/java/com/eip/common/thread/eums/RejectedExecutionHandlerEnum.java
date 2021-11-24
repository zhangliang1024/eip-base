package com.eip.common.thread.eums;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 18:04
 * @Description: 拒绝策略类型
 */
public enum RejectedExecutionHandlerEnum {

    CALLER_RUNS_POLICY("CallerRunsPolicy"),
    ABORT_POLICY("AbortPolicy"),
    DISCARD_POLICY("DiscardPolicy"),
    DISCARD_OLDEST_POLICY("DiscardOldestPolicy")
    ;
    private String rejectedType;

    public String getRejectedType() {
        return rejectedType;
    }

    RejectedExecutionHandlerEnum(String rejectedType){
        this.rejectedType = rejectedType;
    }

    public static boolean exists(String type) {
        for (RejectedExecutionHandlerEnum typeEnum : RejectedExecutionHandlerEnum.values()) {
            if (typeEnum.getRejectedType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
