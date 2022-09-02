package com.eip.cloud.eip.common.sensitive.format;


import com.eip.cloud.eip.common.sensitive.annotation.Sensitive;

public interface Desensitize {

    /**
     * 脱敏处理
     * @param text 敏感信息
     * @return 脱敏后的信息
     */
    String format(String text, Sensitive sensitive);
}