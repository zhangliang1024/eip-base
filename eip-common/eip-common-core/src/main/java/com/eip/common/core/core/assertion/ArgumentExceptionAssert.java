package com.eip.common.core.core.assertion;



import com.eip.common.core.core.exception.BaseRuntimeException;
import com.eip.common.core.core.exception.ArgumentRuntimeException;

import java.text.MessageFormat;

/**
 * 参数断言异常
 */
public interface ArgumentExceptionAssert extends IResponseEnum, Assert {

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
