package com.eip.common.core.core;

import org.springframework.http.HttpStatus;

/**
 * ClassName: BaseHttpCode
 * Function:
 * Date: 2022年11月02 15:53:30
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface BaseHttpCode {

    String BAD_REQUEST = String.valueOf(HttpStatus.BAD_REQUEST);
    String INTERNAL_SERVER_ERROR = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);

}
