package com.eip.base.common.core.core.assertion;

import com.eip.base.common.core.exception.ArgumentRuntimeException;
import com.eip.base.common.core.exception.BaseRuntimeException;

import java.text.MessageFormat;

/**
 * 一般断言异常
 */
public interface CommonExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseRuntimeException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new ArgumentRuntimeException(this, args, msg);
    }

    @Override
    default BaseRuntimeException newException(Throwable cause, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new ArgumentRuntimeException(this, args, msg, cause);
    }
}
