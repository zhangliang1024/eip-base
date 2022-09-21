package com.eip.ability.admin.service;


import com.eip.ability.admin.domain.dto.OptLogDTO;
import com.eip.ability.admin.domain.entity.log.OptLog;
import com.eip.ability.admin.mybatis.supers.SuperService;

/**
 * @author Levin
 */
public interface OptLogService extends SuperService<OptLog> {

    /**
     * 保存操作日志
     *
     * @param dto dto
     */
    void save(OptLogDTO dto);
}
