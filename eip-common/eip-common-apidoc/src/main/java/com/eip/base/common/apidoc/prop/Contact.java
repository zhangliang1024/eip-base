package com.eip.base.common.apidoc.prop;

import lombok.Data;

/**
 * @类描述：维护者信息
 * @创建人：zhiang
 * @创建时间：2021/11/23 14:22
 * @version：V1.0
 */
@Data
public class Contact {

    /**
     * 维护者
     */
    private String name = "default";

    /**
     * 邮箱
     */
    private String email = "springboot@126.com";

    /**
     * 维护者地址
     */
    private String url;


}
