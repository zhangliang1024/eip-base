# SpringBoot自定义注解脱敏


### 数据脱敏
> 指对某些敏感信息通过脱敏规则进行数据的变形，实行敏感数据的可靠保护。
> - 支持自定义处理脱密逻辑
> - `@IgnoreSensitive`注解可作用于类和方法上，支持灵活启用和忽略脱密


### 样例演示
- 实体类
```java
@Data
@Builder
public class User {

    /**
     * 姓名
     */
    @Sensitive(type = SensitiveStrategy.CHINESE_NAME)
    private String username;
    /**
     * 密码
     */
    @Sensitive(type = SensitiveStrategy.PASSWORD)
    private String password;
    /**
     * 邮箱
     */
    @Sensitive(type = SensitiveStrategy.EMAIL)
    private String email;
    /**
     * 固定电话
     */
    @Sensitive(type = SensitiveStrategy.FIXED_PHONE)
    private String fixedPhone;
    /**
     * 手机号
     */
    @Sensitive(type = SensitiveStrategy.MOBILE_PHONE)
    private String phone;
    /**
     * 身份证
     */
    @Sensitive(type = SensitiveStrategy.ID_CARD)
    private String idcard;
    /**
     * 车牌
     */
    @Sensitive(type = SensitiveStrategy.CAR_LICENSE)
    private String carLicense;
    /**
     * 银行卡
     */
    @Sensitive(type = SensitiveStrategy.BANK_CARD)
    private String bankCard;
    /**
     * 地址
     */
    @Sensitive(type = SensitiveStrategy.ADDRESS)
    private String address;
    /**
     * 备注
     */
    @Sensitive(type = SensitiveStrategy.CUSTOMIZE_KEEP_LENGTH, prefix = 2, suffix = 3)
    private String remark;
    /**
     * 地址
     */
    @Sensitive(type = SensitiveStrategy.CUSTOMIZE_WORDS_FILTER)
    @SensitiveWordsFilter(value = {"操","你大爷","废物","不要逼脸"})
    private String message;

    public static User mock(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .idcard("530321199204074611")
                .email("zhangsan@126.com")
                .fixedPhone("01010010010")
                .phone("18888888888")
                .bankCard("9988002866797031")
                .carLicense("京A66666")
                .address("北京市东城区金融街一号")
                .remark("技术的价值在哪里，降本提效。体现在规范性、基础架构上面。让程序井井有条，即时随着业务的发展，不会加大研发的重复开发成本。")
                .message("操你大爷，你说你是不是个废物，真是够不要逼脸的")
                .build();
    }

}
```
- 接口
```java
@RestController
public class SensitiveController {


    @GetMapping("sensitive")
    public ApiResult sensitive() {
        return ApiResult.ok(User.mock("张三三","1234456"));
    }
    
    @IgnoreSensitive
    @GetMapping("sensitive")
    public ApiResult sensitive() {
        return ApiResult.ok(User.mock("张三三","1234456"));
    }

}
```
- 结果
```json
{
    "code": "200",
    "message": "success",
    "data": {
        "username": "张**",
        "password": "*******",
        "email": "z*******@126.com",
        "fixedPhone": "0101*****10",
        "phone": "188****8888",
        "idcard": "5***************11",
        "carLicense": "京A6***6",
        "bankCard": "9988 **** **** 7031",
        "address": "北京市********",
        "remark": "技术*******************************************************成本。",
        "message": "****，你说你是不是个**，真是够****的"
    }
}
```

<img src="http://tva1.sinaimg.cn/large/d1b93a20ly1h6lbmjuofaj20ya0lq109.jpg"/>

### 参考文档 

* [SpringBoot 自定义注解之脱敏注解详解](http://www.cppcns.com/ruanjian/java/446406.html)
* [sensitive-spring-boot-starter](https://github.com/coder-yangge/sensitive-spring-boot-starter)
* [lzhpo/sensitive-spring-boot-starter](https://github.com/lzhpo/sensitive-spring-boot-starter)



