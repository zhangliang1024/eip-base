package com.eip.ability.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.eip.ability.admin.domain.dto.OptLogDTO;
import com.eip.ability.admin.domain.entity.log.OptLog;
import com.eip.ability.admin.mapper.OptLogMapper;
import com.eip.ability.admin.service.OptLogService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
//import com.eip.ability.admin.util.RegionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OptLogServiceImpl extends SuperServiceImpl<OptLogMapper, OptLog> implements OptLogService {

    private final OptLogMapper optLogMapper;

    @Override
    public void save(OptLogDTO dto) {
        DynamicDataSourceContextHolder.push(dto.getDsKey());
        log.info("[日志信息] - {}", JSON.toJSONString(dto));
        final OptLog record = BeanUtil.toBean(dto, OptLog.class);
        //record.setLocation(RegionUtils.getRegion(dto.getIp()));
        this.optLogMapper.insert(record);
        DynamicDataSourceContextHolder.poll();
    }

}
