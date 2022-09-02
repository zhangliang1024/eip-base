package com.eip.cloud.eip.common.sensitive.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @description: 加解密配置信息
 * @date 2020/8/26 10:35
 */
@Data
@ConfigurationProperties(prefix = "spring.sensitive")
public class SensitiveConfigProperties {

    private String type;

    private SecurityProperties security;

    private int maxDeep = 10;

    private List<String> classPackage = new ArrayList<>();

}