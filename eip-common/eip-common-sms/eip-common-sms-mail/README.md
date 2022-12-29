# eip-common-starter-mail
> - 封装发送有邮件细节，提供统一API来实现邮件发送
> - 注入`MailSendService`类 `mailService`，调用`mailService.send(email)`来发送邮件
> - 依赖`Springboot`官方提供的`spring-boot-starter-mail`来实现

## 一、使用方式
> pom.xml中加入依赖
```xml
<dependency>
    <groupId>com.jycloud.sms</groupId>
    <artifactId>jycloud-starter-mail</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
> application.yml 中配置邮件信息
```yaml
spring:
  mail:
    enabled: true
    protocol: stmp # 邮箱协议类型
    host: smtp.163.com #SMTP服务器地址
    username:  socks #登陆账号
    password: 123456 #登陆密码（或授权码）
    properties:
      from: socks@163.com #邮件发信人（即真实邮箱）

```
> 核心逻辑
```java
public class MailSendService {

    /**
     * 发送邮件
     */
    public ApiResult sendMail(MailRequestDTO email) {
        //1.检测邮件内容信息完整
        checkMail(email);
        try {
            //2.发送邮件
            sendMimeMail(email);
            //3.保存邮件
            return saveMail(email);
        } catch (Exception e) {
            
        }
    }

}
```
> 代码实现
```java
@RestController
@RequestMapping("sms/send")
public class MailController{
    @Autowired
    private MailSendService mailService;

    @GetMapping("mail")
    public String sendMail()  {
        MailRequest mail = new MailRequest();
        mail.setFrom("134xxx@qq.com");
        mail.to("xxx@126.com");
        mail.setSubject("我是主题");
        mail.setText("我是内容");
        mail.setSentDate(new Date());
        mailService.sendMail(dto);
    }
}    
```

## 二、实现思路
> 用对象封装邮件的信息
```java
/**
 * 邮件发送对象
 */
public class MailRequest {

    private String id;//邮件id
    private String from;//邮件发送人
    private String to;//邮件接收人（多个邮箱则用逗号","隔开）
    private String subject;//邮件主题
    private String text;//邮件内容
    private boolean html;//邮件内容支持html
    private Date sentDate;//发送时间
    private String cc;//抄送（多个邮箱则用逗号","隔开）
    private String bcc;//密送（多个邮箱则用逗号","隔开）
    private String status;//状态
    private String error;//报错信息
    private MultiFile[] multiFiles;//邮件附件
}
/**
 * 多媒体文件发送
 */
public class MultiFile {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件字节数组
     */
    private byte[] input;
    /**
     * 文件类型
     */
    private String fileType;
}
```
> 文件发送示例
```java
@RestController
public class MailSendController{

    @PostMapping("email/file")
    public ApiResult sendEmailFile(MultipartFile[] files) {
        // HTML 文本
        StringBuilder text = new StringBuilder();
        text.append("<html><head></head>");
        text.append("<body><p>各位老师好：</p></body>");
        text.append("<body><p>本邮件为系统自动发送，如有问题请联系Hello迈迪技术部。</p></body>");
        text.append("<body><p>祝好！</p></body>");
        text.append("</html>");
        MailRequest mail = MailRequest.builder()
                .from("493478910@qq.com")
                .to("zhangliang1024_job@126.com")
                .subject("附件发送")
                .text(text.toString())
                .html(true)
                .build();
        try {
            List<MultiFile> list = new ArrayList<>();
            for (MultipartFile file : files) {
                MultiFile multiFile = new MultiFile();
                multiFile.setFileName(file.getOriginalFilename());
                multiFile.setInput(file.getBytes());
                multiFile.setFileType(file.getContentType());
                list.add(multiFile);
            }
            MultiFile[] array = list.toArray(new MultiFile[list.size()]);
            mail.setMultiFiles(array);
        } catch (Exception e) {
            throw new RuntimeException("send email mutifile error : " + e.getMessage());
        }
        ApiResult apiResult = mailSendService.sendMail(mail);
        return ApiResult.ok();
    }
}
```
> 预留落库接口
```java
/**
* 保存邮件到数据库
*/
public interface IMailService {

    void saveEmail(MailRequestDTO email);

}

public class MailSendService{
    
    //邮件落库要根据实际情况去自行实现
    private ApiResult<?> saveMail(MailRequestDTO email) {
        //将邮件保存到数据库
        mailService.saveEmail(email);
        logger.info("Save Email Success");
        return ApiResult.ok(MailConstant.SendStatus.successMsgPrefix + " Sender " + getMailSendFrom());
    }
}
```
> 邮件落库实现，加注解`@Primary`。因为已经有一默认实现，故需要加此注解，表明使用此实现
```java
@Primary
@Service
public class CustomMailServiceImpl implements IMailService {

    @Override
    public void saveEmail(MailRequestDTO mailRequestDTO) {
       //逻辑落库逻辑
    }
}
```

三、参考文档

* [springboot 发送带excel附件的邮件](https://blog.csdn.net/hlz5857475/article/details/90403100)
* [SpringBoot 发送邮件和附件（实用版）](https://www.jianshu.com/p/5eb000544dd7)