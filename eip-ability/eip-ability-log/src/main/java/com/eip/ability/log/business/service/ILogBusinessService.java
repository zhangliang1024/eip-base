package com.eip.ability.log.business.service;

import com.eip.common.core.log.LogOperationDTO;

@SuppressWarnings("all")
public interface ILogBusinessService  {

    void saveLog(LogOperationDTO operationDTO);
}
