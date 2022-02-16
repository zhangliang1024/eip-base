package com.eip.ability.log.business.service;

import com.eip.ability.log.business.mapper.LogBusinessMapper;
import com.eip.ability.log.business.model.LogOperate;
import com.eip.common.core.log.LogOperationDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * ClassName: LogBusinessServiceImpl
 * Function:
 * Date: 2022年02月15 16:29:34
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Primary
@Service
@SuppressWarnings("all")
public class LogBusinessServiceImpl implements ILogBusinessService {

    @Autowired
    private LogBusinessMapper businessMapper;

    @Override
    public void saveLog(LogOperationDTO operationDTO) {
        LogOperate operate = new LogOperate();
        BeanUtils.copyProperties(operationDTO,operate);
        //businessMapper.insert(operate);
        businessMapper.insertLog(operate);
    }
}
