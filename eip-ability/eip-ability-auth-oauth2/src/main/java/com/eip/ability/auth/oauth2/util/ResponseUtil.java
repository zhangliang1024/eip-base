package com.eip.ability.auth.oauth2.util;

import cn.hutool.json.JSONUtil;
import com.eip.common.core.core.protocol.response.ApiResult;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * ClassName: ResponseUtil
 * Function:
 * Date: 2022年01月18 17:18:08
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class ResponseUtil {

    public static void response(HttpServletResponse response, ApiResult result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(JSONUtil.toJsonStr(result).getBytes(Charset.forName("UTF-8")));
        out.flush();
        out.close();
    }
}
