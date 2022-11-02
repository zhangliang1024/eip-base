package com.eip.ability.admin.exception;

import com.eip.common.core.core.assertion.Assert;
import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.exception.AuthRuntimeException;
import com.eip.common.core.core.exception.BaseRuntimeException;

import java.text.MessageFormat;

/**
 * OAuth2 认证异常
 */
public interface AdminExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseRuntimeException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new AdminRuntimeException(this, args, msg);
    }

    @Override
    default BaseRuntimeException newException(Throwable cause, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new AdminRuntimeException(this, args, msg, cause);
    }
}
