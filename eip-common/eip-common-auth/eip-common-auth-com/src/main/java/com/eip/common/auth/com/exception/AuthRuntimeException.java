package com.eip.common.auth.com.exception;

import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.exception.BaseRuntimeException;

/**
 * ClassName: AuthRuntimeException
 * Function:
 * Date: 2022年01月18 18:57:00
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AuthRuntimeException extends BaseRuntimeException {

    public AuthRuntimeException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public AuthRuntimeException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
