package com.eip.common.log.core.exception;

import com.eip.common.core.core.assertion.Assert;
import com.eip.common.core.core.assertion.IResponseEnum;
import com.eip.common.core.core.exception.ArgumentRuntimeException;
import com.eip.common.core.core.exception.BaseRuntimeException;

import java.text.MessageFormat;

/**
 * ClassName: LogOperateExceptionAssert
 * Function:
 * Date: 2022年02月14 14:29:11
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface LogOperateExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseRuntimeException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new LogOperateRuntimeException(this, args, msg);
    }

    @Override
    default BaseRuntimeException newException(Throwable cause, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new LogOperateRuntimeException(this, args, msg, cause);
    }

}
