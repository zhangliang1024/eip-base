package com.eip.common.core.core.assertion;

import com.eip.common.core.core.exception.BusinessRuntimeException;
import com.eip.common.core.core.exception.BaseRuntimeException;

import java.text.MessageFormat;

/**
 * 业务断言异常
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseRuntimeException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessRuntimeException(this, args, msg);
    }

    @Override
    default BaseRuntimeException newException(Throwable cause, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessRuntimeException(this, args, msg, cause);
    }
}
