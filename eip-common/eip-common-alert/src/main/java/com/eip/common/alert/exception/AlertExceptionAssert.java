package com.eip.common.alert.exception;

import com.eip.common.alert.constant.AlertConstant;
import com.eip.common.core.core.assertion.BusinessExceptionAssert;

/**
 * ClassName: AlertExceptionAssert
 * Function:
 * Date: 2021年12月15 11:37:00
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public enum AlertExceptionAssert implements BusinessExceptionAssert {

    ALERT_SEND_SUCCESS(String.valueOf(AlertConstant.SUCCES_RESULT_CODE), "alert success"),

    ALERT_SEND_ERROR(AlertConstant.SYSTEM_ERROR_CODE, "alert error"),


    ;

    private String code;

    private String message;

    AlertExceptionAssert(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
