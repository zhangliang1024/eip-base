package com.eip.cloud.eip.common.sensitive.codec;

public interface Encrypt {

    /**
     * <p>加密处理</p>
     * @param data 原始数据
     * @return 加密信息
     */
    byte[] encrypt(byte[] data);

}