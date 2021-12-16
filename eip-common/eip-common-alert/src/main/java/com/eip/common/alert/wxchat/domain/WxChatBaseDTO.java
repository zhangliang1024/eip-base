package com.eip.common.alert.wxchat.domain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.eip.common.alert.BaseDTO;
import com.eip.common.alert.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送给微信服务器的消息对象
 */
@Data
public class WxChatBaseDTO extends BaseDTO {

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private WxChatText text;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private WxChatMarkdown markdown;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private WxChatImage image;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Map<String, List<WxChatNews>> news;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private WxChatFile file;

    public WxChatBaseDTO(WxChatText wxChatText) {
        //文本内容，超过2048个字节，自动转为txt文件发送
        if (this.getBytesLength(wxChatText.getContent()) > 2048) {
            this.msgtype = MessageType.FILE.getType();
            this.file = this.getFileMsg(wxChatText.getContent());
        } else {
            this.msgtype = MessageType.TEXT.getType();
            this.text = wxChatText;
        }
    }

    public WxChatBaseDTO(WxChatMarkdown wxChatMarkdown) {
        //文本内容，超过4096个字节，自动转为txt文件发送
        if (this.getBytesLength(wxChatMarkdown.getContent()) > 4096) {
            this.msgtype = MessageType.FILE.getType();
            this.file = this.getFileMsg(wxChatMarkdown.getContent());
        } else {
            this.msgtype = MessageType.MARKDOWN.getType();
            this.markdown = wxChatMarkdown;
        }
    }

    public WxChatBaseDTO(WxChatImage wxChatImage) {
        this.msgtype = MessageType.IMAGE.getType();
        this.image = wxChatImage;
    }

    public WxChatBaseDTO(WxChatNews wxChatNews) {
        this.msgtype = MessageType.NEWS.getType();
        List<WxChatNews> wxChatNewsList = new ArrayList<WxChatNews>();
        wxChatNewsList.add(wxChatNews);
        this.news = new HashMap<>();
        this.news.put("articles", wxChatNewsList);
    }

    public WxChatBaseDTO(List<WxChatNews> wxChatNewsList) {
        this.msgtype = MessageType.NEWS.getType();
        this.news = new HashMap<>();
        this.news.put("articles", wxChatNewsList);
    }

    public WxChatBaseDTO(WxChatFile wxChatFile) {
        this.msgtype = MessageType.FILE.getType();
        this.file = wxChatFile;
    }

    /**
     * 通过内容生成文件对象
     */
    private WxChatFile getFileMsg(String content) {
        File file = FileUtil.createTempFile(FileUtil.getTmpDir(), true);
        file = FileUtil.rename(file, String.format("长消息%s.txt", DateUtil.now()), true);
        FileUtil.writeString(content, file, StandardCharsets.UTF_8);
        WxChatFile wxChatFile = new WxChatFile();
        wxChatFile.setFile(file);
        return wxChatFile;
    }

    /**
     * 获取内容字节长度
     */
    private int getBytesLength(String content) {
        return content.getBytes(StandardCharsets.UTF_8).length;
    }


}
