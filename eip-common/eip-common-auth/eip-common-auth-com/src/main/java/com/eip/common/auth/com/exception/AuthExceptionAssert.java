package com.eip.common.auth.com.exception;

import com.eip.common.core.core.assertion.Assert;
import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.exception.BaseRuntimeException;

import java.text.MessageFormat;

/**
 * ClassName: AuthExceptionAssert
 * Function:
 * Date: 2022年01月18 18:55:46
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface AuthExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseRuntimeException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new AuthRuntimeException(this, args, msg);
    }

    @Override
    default BaseRuntimeException newException(Throwable cause, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new AuthRuntimeException(this, args, msg, cause);
    }
}
