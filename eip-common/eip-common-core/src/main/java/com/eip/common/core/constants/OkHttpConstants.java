package com.eip.common.core.constants;

import okhttp3.MediaType;

/**
 * ClassName: OkHttpConstants
 * Function:
 * Date: 2022年02月10 17:50:38
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface OkHttpConstants {

    String QUESTION_SEPARATE = "?";

    String PARAM_SEPARATE = "&";

    String KV_SEPARATE = "=";

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    MediaType XML = MediaType.parse("application/xml; charset=utf-8");
}
