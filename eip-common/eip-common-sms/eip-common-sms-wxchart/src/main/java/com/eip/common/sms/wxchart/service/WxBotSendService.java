package com.eip.common.sms.wxchart.service;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.eip.common.sms.wxchart.common.MsgType;
import com.eip.common.sms.wxchart.common.WxBotBuilder;
import com.eip.common.sms.wxchart.config.WxBotProperties;
import com.eip.common.sms.wxchart.model.*;

import java.util.Collections;
import java.util.List;

/**
 * ClassName: WxBotController
 * Function: 企业微信机器人
 * Date: 2022年12月28 17:12:40
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class WxBotSendService {

    /**
     * 超时时间
     */
    private int timeout = WxBotProperties.DEFAULT_TIMEOUT;
    /**
     * 企业微信群中获取的webhook地址
     */
    private String webhook;


    public WxBotSendService(String webhook) {
        this.webhook = webhook;
    }

    public WxBotSendService(String webhook, int timeout) {
        this.webhook = webhook;
        this.timeout = timeout;
    }

    /**
     * 发送文本消息
     */
    public void send(String content) {
        send(content, false);
    }

    /**
     * 发送文本消息
     */
    public void send(String content, boolean atAll) {
        Text text = new Text();
        text.setContent(content);
        // @所有人
        if (atAll) {
            text.setMentioned_list(Collections.singletonList("@all"));
        }
        WxBotBuilder builder = new WxBotBuilder(text);
        doPost(builder);
    }

    /**
     * 发送文本消息
     */
    public void send(Text text) {
        if (text.isAtAll()) {
            text.setMentioned_list(Collections.singletonList("@all"));
        }
        WxBotBuilder builder = new WxBotBuilder(text);
        doPost(builder);
    }

    /**
     * 发送markdown消息
     */
    public void send(Markdown markdown) {
        WxBotBuilder builder = new WxBotBuilder(markdown);
        doPost(builder);
    }

    /**
     * 发送图片消息
     */
    public void send(Image image) {
        WxBotBuilder builder = new WxBotBuilder(image);
        doPost(builder);
    }

    /**
     * 发送图文消息
     */
    public void send(Article article) {
        WxBotBuilder builder = new WxBotBuilder(article);
        doPost(builder);
    }

    /**
     * 发送多个图文消息
     */
    public void send(List<Article> articles) {
        WxBotBuilder builder = new WxBotBuilder(articles);
        doPost(builder);
    }

    /**
     * 发送文件消息
     */
    public void send(WxFile wxFile) {
        WxBotBuilder builder = new WxBotBuilder(wxFile);
        doPost(builder);
    }

    public void send(WxBotBuilder builder) {
        doPost(builder);
    }

    /**
     * 请求微信接口，实现消息的发送
     */
    public void doPost(WxBotBuilder builder) {
        try {
            // 发送文件，先获取文件mediaId
            if (MsgType.FILE.getTypeName().equals(builder.getMsgtype())) {
                getMediaId(builder);
            }
            // 调用微信机器人接口
            String request = JSONUtil.toJsonStr(builder);
            String response = HttpRequest.post(webhook)
                    .header(Header.CONTENT_TYPE, ContentType.JSON.toString())
                    .body(request)
                    .timeout(timeout)
                    .execute()
                    .body();
            JSONObject jsonObject = JSONUtil.parseObj(response);
            if (jsonObject.getInt("errcode") != 0) {
                throw new RuntimeException(jsonObject.getStr("errmsg"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取文件mediaId
     */
    private void getMediaId(WxBotBuilder builder) {
        WxFile wxFile = builder.getFile();
        // 文件类型消息，且mediaId为空，则先上传文件获取mediaId
        if (wxFile != null && wxFile.getMedia_id() == null) {
            String uploadUrl = webhook.replace("send", "upload_media") + "&type=file";
            String response = HttpRequest.post(uploadUrl)
                    .header(Header.CONTENT_TYPE, ContentType.MULTIPART.toString())
                    .form("media", wxFile.getFile())
                    .timeout(timeout)
                    .execute()
                    .body();
            JSONObject jsonObject = JSONUtil.parseObj(response);
            if (jsonObject.getInt("errcode") != 0) {
                throw new RuntimeException(jsonObject.getStr("errmsg"));
            } else {
                wxFile.setMedia_id(jsonObject.getStr("media_id"));
            }
        }
    }

}

