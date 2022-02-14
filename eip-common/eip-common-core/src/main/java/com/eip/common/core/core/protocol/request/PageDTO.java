package com.eip.common.core.core.protocol.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取列表的分页请求数据
 */
@Data
@ApiModel(value = "获取列表的分页请求数据")
public class PageDTO extends RequestDTO {

    /**
     * 当前页码,从1开始，默认1
     */
    @ApiModelProperty(value = "页号", dataType = "int", notes = "首页从1开始")
    protected int pageNum = 1;
    /**
     * 分页大小,默认10
     */
    @ApiModelProperty(value = "页码", dataType = "int", notes = "默认一页显示10个数据")
    protected int pageSize = 10;


}
