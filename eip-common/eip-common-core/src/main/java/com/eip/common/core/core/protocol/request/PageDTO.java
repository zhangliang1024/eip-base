package com.eip.common.core.core.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 获取列表的分页请求数据
 */
@Data
@Schema(defaultValue = "获取列表的分页请求数据")
public class PageDTO extends RequestDTO {

    /**
     * 当前页码,从1开始，默认1
     */
    @Schema(defaultValue = "页号", type = "int", description = "首页从1开始")
    protected int pageNum = 1;
    /**
     * 分页大小,默认10
     */
    @Schema(defaultValue = "页码", type = "int", description = "默认一页显示10个数据")
    protected int pageSize = 10;


}
