package com.eip.common.thread.eums;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: 队列类型
 */
public enum QueueTypeEnum {

    LINKED_BLOCKING_QUEUE("LinkedBlockingQueue"),
    SYNCHRONOUS_QUEUE("SynchronousQueue"),
    ARRAY_BLOCKING_QUEUE("ArrayBlockingQueue"),
    DELAY_QUEUE("DelayQueue"),
    LINKED_TRANSFER_DEQUE("LinkedTransferQueue"),
    LINKED_BLOCKING_DEQUE("LinkedBlockingDeque"),
    PRIORITY_BLOCKING_QUEUE("PriorityBlockingQueue")
    ;

    private String queueType;

    QueueTypeEnum(String queueType){
        this.queueType = queueType;
    }

    public String getQueueType() {
        return queueType;
    }

    public static boolean exists(String queueTyep){
        for (QueueTypeEnum queueTypeEnum : QueueTypeEnum.values()) {
            if(queueTypeEnum.getQueueType().equals(queueTyep)){
                return true;
            }
        }
        return false;
    }
}
