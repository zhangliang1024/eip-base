package com.eip.common.apidoc.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 14:15
 * @version：V1.0
 */
@Data
@ConfigurationProperties(prefix = "springdoc.eip-docs")
public class ApidocInfo {

    /**
     * 组名
     */
    private String groupName = "default";

    /**
     * 暴露接口地址路径，最终由配置文件中为结果
     */
    private List<String> pathsToMatch = Arrays.asList("/**");

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
    private String licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0.html";

    /**
     * 维护者
     */
    private Contact contact = new Contact();

    private ExternalDocs externalDocs = new ExternalDocs();


}
