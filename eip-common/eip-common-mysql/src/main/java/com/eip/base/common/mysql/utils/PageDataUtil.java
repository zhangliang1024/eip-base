package com.eip.base.common.mysql.utils;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.base.common.core.core.protocol.response.PageData;

import java.util.List;

public class PageDataUtil {

    public static PageData wrapPageDataDTO(Page page, List list) {
        PageData data = new PageData();
        data.setTotal(page.getTotal());
        data.setList(list);
        return data;
    }
}
