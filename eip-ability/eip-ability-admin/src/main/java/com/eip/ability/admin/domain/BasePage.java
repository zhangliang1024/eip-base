package com.eip.ability.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 分页参数
 *
 * @author Levin
 * @since 2020-07-08
 */
@Slf4j
@Data
public class BasePage {

    @Parameter(description = "当前页码", example = "1")
    private long pageNum = 1;

    @Parameter(description = "页面大小", example = "20")
    private long pageSize = 20;


    @JsonIgnore
    public <T> Page<T> buildPage() {
        return new Page<T>(pageNum, pageSize);
    }
}
