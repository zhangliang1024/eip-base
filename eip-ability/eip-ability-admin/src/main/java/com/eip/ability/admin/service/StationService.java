package com.eip.ability.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.PageRequest;
import com.eip.ability.admin.domain.dto.StationPageDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Station;
import com.eip.ability.admin.mybatis.supers.SuperService;

/**
 * <p>
 * 业务接口
 * 岗位
 * </p>
 *
 * @author Levin
 * @since 2019-07-22
 */
public interface StationService extends SuperService<Station> {
    /**
     * 按权限查询岗位的分页信息
     *
     * @param params params
     * @param data data
     * @return Station
     */
    IPage<Station> findStationPage(PageRequest params, StationPageDTO data);
}
