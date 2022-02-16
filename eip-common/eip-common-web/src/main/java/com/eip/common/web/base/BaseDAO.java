package com.eip.common.web.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDAO<Entity> extends BaseMapper<Entity> {

    int insert(Entity entity);

    long update(Entity entity);

}
