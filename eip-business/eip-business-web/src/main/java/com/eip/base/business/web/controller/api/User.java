package com.eip.base.business.web.controller.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @项目名称：springboot
 * @包名：com.zhliang.springbootapiui.controller
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2020/5/5 16:08
 * @version：V1.0
 */
@Schema
public class User {

    @Schema(name = "用户id")
    private Integer id;
    @Schema(name = "用户名")
    private String username;
    @Schema(name = "用户地址")
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
