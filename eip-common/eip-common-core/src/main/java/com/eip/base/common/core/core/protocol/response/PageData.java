package com.eip.base.common.core.core.protocol.response;

import lombok.Data;

import java.util.List;

/**
 * 分页返回数据
 */
@Data
public class PageData<T> {

    /**
     * 当前页码
     */
    private long pageNum;
    /**
     * 每页数量
     */
    private long pageSize;
    /**
     * 数据总计
     */
    private long total;
    /**
     * 结果集
     */
    private List<T> list;


}
