package com.eip.base.common.apidoc.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 14:15
 * @version：V1.0
 */
@Data
@ConfigurationProperties(prefix = "eip.api.doc.swagger")
public class ApidocInfo {


    /**
     * 是否启用swagger
     */
    private boolean enabled = false;

    /**
     * 组名
     */
    private String groupName = "default";

    /**
     * host地址
     */
    private String host;

    /**
     * 项目标题
     */
    private String title;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目版本
     */
    private String version = "1.0.0";

    /**
     * 许可证
     */
    private String license = "Apache License, Version 2.0";

    /**
     * 许可证url
     */
    private String licenseUrl  = "https://www.apache.org/licenses/LICENSE-2.0.html";

    /**
     * 维护者
     */
    private Contact contact = new Contact();

    /**
     * 包扫描
     */
    private String basePackage = "com.eip.base.business";

    /**
     * 需要处理的基础URL规则，默认：/**
     */
    private String basePath;

    /**
     * 需要排除的URL规则，默认：空
     */
    private String excludePath;

}
