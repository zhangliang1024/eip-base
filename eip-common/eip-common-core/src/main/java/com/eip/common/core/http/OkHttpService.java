package com.eip.common.core.http;

import cn.hutool.core.map.MapUtil;
import com.eip.common.core.constants.OkHttpConstants;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * ClassName: OkHttpUtils
 * Function:
 * Date: 2022年01月26 13:14:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Component
public class OkHttpService {

    @Autowired
    private OkHttpClient okHttpClient;

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGetWithHeaders(String url, Map<String, String> headers) {
        return doGet(url, null, headers);
    }


    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append(OkHttpConstants.QUESTION_SEPARATE)
                            .append(key)
                            .append(OkHttpConstants.KV_SEPARATE)
                            .append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append(OkHttpConstants.PARAM_SEPARATE)
                            .append(key)
                            .append(OkHttpConstants.KV_SEPARATE)
                            .append(params.get(key));
                }
            }
        }
        Request.Builder builder = getBuilderWithHeaders(headers);
        Request request = builder.url(sb.toString()).build();

        log.info("[eip-okhttp] - get request url [{}]", sb.toString());
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doPost(String url, Map<String, String> params, Map<String, String> headers) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                formBuilder.add(key, params.get(key));
            }
        }
        Request.Builder builder = getBuilderWithHeaders(headers);

        Request request = builder.url(url).post(formBuilder.build()).build();
        log.info("[eip-okhttp] - post request url [{}]", url);

        return execute(request);
    }

    /**
     * 获取request Builder
     *
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return
     */
    private Request.Builder getBuilderWithHeaders(Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (!MapUtil.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }


    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        log.info("[eip-okhttp] -post request url [{}]", url);
        return executePost(url, json, OkHttpConstants.JSON, null);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url     请求url地址
     * @param json    请求数据, json 字符串
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doPostJsonWithHeaders(String url, String json, Map<String, String> headers) {
        log.info("[eip-okhttp] - post request url [{}]", url);
        return executePost(url, json, OkHttpConstants.JSON, headers);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        log.info("[eip-okhttp] - post request url [{}]", url);
        return executePost(url, xml, OkHttpConstants.XML, null);
    }

    private String executePost(String url, String data, MediaType contentType, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(contentType, data.getBytes(StandardCharsets.UTF_8));
        Request.Builder builder = getBuilderWithHeaders(headers);
        Request request = builder.url(url).post(requestBody).build();

        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
        }
        return Strings.EMPTY;
    }

}

