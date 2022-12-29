# eip-sms-starter-wxchart
> 企业微信机器人发送消息

## 一、接入方式
- 依赖
```xml
<dependency>
    <groupId>com.eip.cloud</groupId>
    <artifactId>eip-common-sms-wxchart</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
- 配置文件
```yaml
eip:
  mail:
    enabled: true
  wx-chart:
    enabled: true
    webhook: https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=bde3d879-078d-42a4-ae9c-xxxxxxxx
```

## 二、核心逻辑
> - `WxBotSendService` 封装发送的逻辑
> - `WxBotBuilder` 构建不同消息类型实现

```java
public class WxBotSendService {

    // ...
    
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

    // ...
}
```
```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxBotBuilder {

    // ...

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

    // ...
}
```

## 三、案例演示

```java
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
        botService.send(markdown);
        return ApiResult.ok();
    }

}
```
- 发送结果

<img src="http://tva1.sinaimg.cn/large/d1b93a20ly1h9khqt6m2lj20q20g7dik.jpg"/>

<img src="http://tva1.sinaimg.cn/large/d1b93a20ly1h9khuqtpbyj20ln0a0wgc.jpg"/>


## 三、参考文档

[wechat-work-bot](https://github.com/lqccan/wechat-work-bot)
