# 阿里云短信工具封装starter
## 概述
包含阿里云短信客户端一些常用的配置和逻辑。建议需要用到阿里云短信的工程都引入。

## 引入
```
<dependency>
    <groupId>com.apifan.framework</groupId>
    <artifactId>aliyunsms-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 配置说明
```
aliyunsms:
  access-key-id: 123456789
  access-key-secret: asdfghjkl
  region-id: cn-shanghai
```

## 使用范例
```
@Autowired
private AliyunSmsHelper aliyunSmsHelper;
    
//...


public void send(){
    SmsMsgVO sms = new SmsMsgVO();
    //建议使用唯一流水号，便于后续查询
    sms.setSerialId(String.valueOf(System.currentTimeMillis()));
    //如有多个号码，依次添加
    sms.addMobile("13895279527");    
    
    //设置短信签名（可在短信控制台中找到）
    sms.setSignName("某某APP");
    
    //设置短信模板编号（可在短信控制台中找到）
    sms.setTemplateCode("SMS_12345678");
    
    //例如短信模板为： 尊敬的${name}，您的当前可用余额为${balance}元
    //设置以下变量
    Map<String, String> smsVars = new HashMap<>();
    smsVars.put("name", "张三");
    smsVars.put("balance", "99.50");
    sms.setVars(smsVars);
    boolean result = aliyunSmsHelper.send(sms);
    
    //后续处理逻辑... ...
    
}

```