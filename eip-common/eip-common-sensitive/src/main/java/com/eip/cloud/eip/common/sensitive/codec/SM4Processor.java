package com.eip.cloud.eip.common.sensitive.codec;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SM4;

public class SM4Processor implements SecurityProcessor {

    private String secret;

    private SM4 sm4;

    public SM4Processor(String secret) {
        this.secret = secret;
        this.sm4 = new SM4(SecureUtil.decode(secret));
    }

    @Override
    public byte[] decrypt(String text) {
        return sm4.decrypt(text);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return sm4.encrypt(data);
    }
}