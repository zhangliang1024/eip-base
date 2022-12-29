package com.eip.common.sms.wxchart.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.eip.common.sms.wxchart.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: WxBotController
 * Function: WxBot构建
 * Date: 2022年12月28 17:12:40
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxBotBuilder {

    private static final int DEFAULT_LENTH = 2048;
    /**
     * 消息类型
     */
    private String msgtype;

    private Text text;

    private Markdown markdown;

    private Image image;

    private Map<String, List<Article>> news;

    private WxFile file;


    public WxBotBuilder(Text text) {
        // 文本内容，超过2048个字节，自动转为txt文件发送
        if (this.getBytesLength(text.getContent()) > DEFAULT_LENTH) {
            this.msgtype = MsgType.FILE.getTypeName();
            this.file = this.getFileMsg(text.getContent());
        } else {
            this.msgtype = MsgType.TEXT.getTypeName();
            this.text = text;
        }
    }

    public WxBotBuilder(Markdown markdown) {
        // 文本内容，超过4096个字节，自动转为txt文件发送
        if (this.getBytesLength(markdown.getContent()) > (DEFAULT_LENTH * 2)) {
            this.msgtype = MsgType.FILE.getTypeName();
            this.file = this.getFileMsg(markdown.getContent());
        } else {
            this.msgtype = MsgType.MARKDOWN.getTypeName();
            this.markdown = markdown;
        }
    }

    public WxBotBuilder(Image image) {
        this.msgtype = MsgType.IMAGE.getTypeName();
        this.image = image;
    }

    public WxBotBuilder(Article article) {
        this.msgtype = MsgType.NEWS.getTypeName();
        List<Article> articles = new ArrayList<Article>();
        articles.add(article);
        this.news = new HashMap<String, List<Article>>();
        this.news.put("articles", articles);
    }

    public WxBotBuilder(List<Article> articles) {
        this.msgtype = MsgType.NEWS.getTypeName();
        this.news = new HashMap<String, List<Article>>();
        this.news.put("articles", articles);
    }

    public WxBotBuilder(WxFile wxFile) {
        this.msgtype = MsgType.FILE.getTypeName();
        this.file = wxFile;
    }

    /**
     * 通过内容生成文件对象
     */
    private WxFile getFileMsg(String content) {
        java.io.File file = FileUtil.createTempFile(FileUtil.getTmpDir(), true);
        file = FileUtil.rename(file, String.format("长消息%s.txt", DateUtil.now()), true);
        FileUtil.writeString(content, file, StandardCharsets.UTF_8);
        WxFile wxFileMsg = new WxFile();
        wxFileMsg.setFile(file);
        return wxFileMsg;
    }

    /**
     * 获取内容字节长度
     */
    private int getBytesLength(String content) {
        return content.getBytes(StandardCharsets.UTF_8).length;
    }

}
