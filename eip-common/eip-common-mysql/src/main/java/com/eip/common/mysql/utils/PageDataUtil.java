package com.eip.common.mysql.utils;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.common.core.core.protocol.response.PageResult;

import java.util.List;

public class PageDataUtil {

    public static PageResult wrapPageDataDTO(Page page, List list) {
        PageResult data = new PageResult();
        data.setTotal(page.getTotal());
        data.setList(list);
        return data;
    }
}
