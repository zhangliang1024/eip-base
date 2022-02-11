package com.eip.common.log.core.service;

import com.eip.common.log.core.model.LogOperationDTO;

/**
 * ClassName: LogService
 * Function:
 * Date: 2022年02月11 14:22:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface LogService {

    boolean createLog(LogOperationDTO operationDTO) throws Exception;
}
