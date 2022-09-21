package com.eip.ability.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.DataScope;
import com.eip.ability.admin.domain.PageRequest;
import com.eip.ability.admin.domain.dto.StationPageDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Station;
import com.eip.ability.admin.domain.enums.DataScopeType;
import com.eip.ability.admin.mapper.StationMapper;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.mybatis.wraps.query.LbqWrapper;
import com.eip.ability.admin.service.StationService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 业务实现类
 * 岗位
 * </p>
 *
 * @author Levin
 * @since 2019-07-22
 */
@Slf4j
@Service
public class StationServiceImpl extends SuperServiceImpl<StationMapper, Station> implements StationService {


    @Override
    public IPage<Station> findStationPage(PageRequest params, StationPageDTO data) {
        Station station = BeanUtil.toBean(data, Station.class);
        final LbqWrapper<Station> wrapper = Wraps.<Station>lbQ().like(Station::getName, station.getName())
                .like(Station::getDescription, station.getDescription()).eq(Station::getType,data.getType())
                .eq(Station::getStatus,data.getStatus()).eq(Station::getOrgId, station.getOrgId())
                .eq(Station::getStatus, station.getStatus()).orderByAsc(Station::getSequence);
        return baseMapper.findStationPage(params.buildPage(), wrapper, DataScope.builder().scopeType(DataScopeType.ALL).build());
    }
}
