package com.eip.sample.sms.web;

import cn.hutool.core.io.FileUtil;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.sms.wxchart.common.WxBotBuilder;
import com.eip.common.sms.wxchart.model.*;
import com.eip.common.sms.wxchart.service.WxBotSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: WxBotController
 * Function:
 * Date: 2022年12月28 17:12:40
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RestController
@RequestMapping("sms/wxbot")
public class WxBotController {

    @Autowired
    private WxBotSendService botService;

    /**
     * 文字
     */
    @GetMapping("text")
    public ApiResult text() {
        Text text = new Text();
        text.setContent("文字发送\nhttp://www.baidu.com");
        botService.send(text);
        return ApiResult.ok();
    }

    /**
     * markdown
     */
    @GetMapping("markdown")
    public ApiResult markdown() {
        Markdown markdown = new Markdown();
        StringBuilder contenct = new StringBuilder();
        contenct.append("实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。\n");
        contenct.append("         >类型:<font color=\"comment\">用户反馈</font>\n");
        contenct.append("         >普通用户反馈:<font color=\"comment\">117例</font>\n");
        contenct.append("         >VIP用户反馈:<font color=\"comment\">15例</font>");
        markdown.setContent(contenct.toString());
        // markdown.setContent("实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。\n" +
        //         "         >类型:<font color=\"comment\">用户反馈</font>\n" +
        //         "         >普通用户反馈:<font color=\"comment\">117例</font>\n" +
        //         "         >VIP用户反馈:<font color=\"comment\">15例</font>");
        botService.send(markdown);
        return ApiResult.ok();
    }

    /**
     * 图片
     */
    @GetMapping("image")
    public ApiResult image() {
        Image image = new Image();
        // 本地文件
        image.setFile(new File(this.getPath("/doc/0.png")));
        botService.send(image);
        // 网络文件
        image.setFile("http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png");
        botService.send(image);
        return ApiResult.ok();
    }

    /**
     * 获取地址
     */
    public String getPath(String path) {
        return this.getClass().getResource(path).getPath();
    }

    /**
     * 图文
     */
    @GetMapping("article")
    public ApiResult article() {
        Article article = new Article();
        article.setTitle("图文");
        article.setDescription("这是一条图文消息");
        article.setUrl("https://www.baidu.com");
        article.setPicurl("http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png");
        botService.send(article);
        return ApiResult.ok();
    }

    /**
     * 多个图文
     */
    @GetMapping("articles")
    public ApiResult articles() {
        List<Article> list = new ArrayList<Article>();
        for (int i = 1; i <= 3; i++) {
            Article article = new Article();
            article.setTitle("图文" + i);
            article.setDescription("这是一条图文消息");
            article.setUrl("https://www.baidu.com");
            article.setPicurl("http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png");
            list.add(article);
        }
        botService.send(list);
        return ApiResult.ok();
    }

    /**
     * 文件
     */
    @GetMapping("file")
    public ApiResult fileTest() {
        WxFile file = new WxFile();
        // 本地文件
        file.setFile(new File(this.getPath("/doc/abc.xlsx")));
        botService.send(file);
        return ApiResult.ok();
    }


    @PostMapping("wxchart")
    public ApiResult sendWxChart(@RequestBody WxBotBuilder botMsg) {
        log.info("send wxchart type: {}", botMsg.getMsgtype());
        botService.doPost(botMsg);
        return ApiResult.ok();
    }


    @PostMapping("wxchart/file")
    public ApiResult sendWxChartFile(MultipartFile[] files) {
        WxFile wxFileMsg = new WxFile();
        for (MultipartFile multipart : files) {
            byte[] bytes = new byte[0];
            try {
                bytes = multipart.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file = FileUtil.createTempFile(FileUtil.getTmpDir(), true);
            String originalFilename = multipart.getOriginalFilename(); // 文件名及后缀信息
            // String fileName = originalFilename.substring(0, originalFilename.lastIndexOf(".")); // 文件名
            // String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 文件后缀
            // String fileType = multipart.getContentType(); // 文件类型
            // long size = multipart.getSize(); // 文件大小
            // file = FileUtil.rename(file, String.format("%s-%s%s", fileName, DateUtil.today(), fileSuffix), true);
            file = FileUtil.rename(file, originalFilename, true);
            FileUtil.writeBytes(bytes, file);
            wxFileMsg.setFile(file);
        }
        botService.send(wxFileMsg);
        return ApiResult.ok();
    }
}
