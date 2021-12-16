package com.eip.common.alert;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.eip.common.alert.constant.AlertConstant;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import static com.eip.common.alert.exception.AlertExceptionAssert.ALERT_SEND_ERROR;

/**
 * ClassName: HttpAgent
 * Function:
 * Date: 2021年12月16 13:08:45
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class HttpAgent {

    public static void sendRequest(String request, String url) {
        try {
            String response = HttpRequest.post(url)
                    .header(Header.CONTENT_TYPE, ContentType.JSON.toString())
                    .body(request)
                    .timeout(AlertConstant.REQUEST_TIME_OUT)
                    .execute()
                    .body();
            JSONObject result = JSONUtil.parseObj(response);
            ALERT_SEND_ERROR.assertIsTrue(
                    result.get("errcode").equals(AlertConstant.SUCCES_RESULT_CODE),
                    result.getStr("errmsg"));
        } catch (Exception e) {
            log.error("[eip-alert] request error : {}", e);
            ALERT_SEND_ERROR.assertFail(e);
        }
    }

    public static String sendRequest(File file, String uploadRrl) {
        try {
            String response = HttpRequest.post(uploadRrl)
                    .header(Header.CONTENT_TYPE, ContentType.MULTIPART.toString())
                    .form("media", file)
                    .timeout(AlertConstant.REQUEST_TIME_OUT)
                    .execute()
                    .body();
            JSONObject result = JSONUtil.parseObj(response);
            ALERT_SEND_ERROR.assertIsTrue(
                    result.get("errcode").equals(AlertConstant.SUCCES_RESULT_CODE),
                    result.getStr("errmsg"));
            return result.getStr("media_id");
        } catch (Exception e) {
            log.error("[eip-alert] request error : {}", e);
            ALERT_SEND_ERROR.assertFail(e);
        }
        return null;
    }
}
