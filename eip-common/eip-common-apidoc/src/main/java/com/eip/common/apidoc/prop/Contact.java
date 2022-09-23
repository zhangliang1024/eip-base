package com.eip.common.apidoc.prop;

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
    private String name = "ZhLiang";

    /**
     * 邮箱
     */
    private String email = "zhangliang1024_job@126.com";

    /**
     * 维护者地址
     */
    private String url = "https://github.com/zhangliang1024/eip-base";


}
