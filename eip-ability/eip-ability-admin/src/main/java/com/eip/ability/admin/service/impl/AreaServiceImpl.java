package com.eip.ability.admin.service.impl;

import com.eip.ability.admin.domain.entity.common.AreaEntity;
import com.eip.ability.admin.mapper.AreaMapper;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.AreaService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends SuperServiceImpl<AreaMapper, AreaEntity> implements AreaService {

    @Override
    public List<AreaEntity> listArea(Integer parentId) {
        return baseMapper.listArea(parentId);
    }

    @Override
    public void saveOrUpdateArea(AreaEntity area) {
        final long count = count(Wraps.<AreaEntity>lbQ().eq(AreaEntity::getId, area.getId()));
        if (count == 0) {
            baseMapper.insert(area);
        } else {
            baseMapper.updateById(area);
        }
    }


}
