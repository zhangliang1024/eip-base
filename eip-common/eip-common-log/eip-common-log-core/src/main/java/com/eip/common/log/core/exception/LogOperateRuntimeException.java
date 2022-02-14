package com.eip.common.log.core.exception;

import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.exception.BaseRuntimeException;

/**
 * ClassName: LogOperateRuntimeException
 * Function:
 * Date: 2022年02月14 14:31:03
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class LogOperateRuntimeException extends BaseRuntimeException {

    public LogOperateRuntimeException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public LogOperateRuntimeException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }

}
