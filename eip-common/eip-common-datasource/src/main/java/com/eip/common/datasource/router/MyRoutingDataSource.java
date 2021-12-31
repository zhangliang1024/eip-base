package com.eip.common.datasource.router;

import com.eip.common.datasource.context.DBContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据库路由
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.getContextHolder();
    }
}
