package com.eip.cloud.eip.common.sensitive.codec;

public interface Decrypt {

    /**
     * <p>解密处理</p>
     * @param text 加密数据
     * @return 原始信息
     */
    byte[] decrypt(String text);
}