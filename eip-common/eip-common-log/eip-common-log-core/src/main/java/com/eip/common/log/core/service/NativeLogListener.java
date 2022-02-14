package com.eip.common.log.core.service;

import com.eip.common.core.log.LogOperationDTO;

/**
 * ClassName: NativeLogListener
 * Function:
 * Date: 2022年02月11 14:23:46
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public abstract class NativeLogListener {

    public abstract void createLog(LogOperationDTO operationDTO) throws Exception;

}
