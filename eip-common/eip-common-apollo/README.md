


### 一、使用默认加密方式进行配置加密
> 因`jasypt-spring-boot-starter`-`2.0.0`版本以上会导致动态刷新失效，故默认使用`2.0.0`版本
- `pom.xml`版本
```xml
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-client</artifactId>
    <version>1.9.2</version>
</dependency>
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-core</artifactId>
    <version>1.9.2</version>
</dependency>
<!--jasypt加密-->
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

- 基于默认加解密逻辑，可以直接根据以下配置来实现配置的加解密
> 指定加解密用到的`password` 即可。`password`可通过环境变量方式来指定，避免泄露
```yaml
jasypt:
  encryptor:
    # 指定加密密钥，生产环境请放到启动参数里面
    password: ${JASYPT_PASSWORD:yinjihaunkey}
    # 指定解密算法，需要和加密时使用的算法一致
    algorithm: PBEWithMD5AndDES
    # 指定initialization vector类型
    iv-generator-classname: org.jasypt.iv.NoIvGenerator
    # 支持扩展前后缀
    #property:
    #  prefix: 
    #  suffix: 

# 加密配置使用方式
user:
  name: ENC(cH5hcjWTDomE44riMs8d06ncNfuLG8oxbSDYD5y3D9E=)
```

- 运行 
```bash
java -jar -Djasypt.encryptor.password=${JASYPT_PASSWORD} xxx.jar
```


### 二、代码示例
```java
@Data
@Configuration
@ConfigurationProperties(prefix = "eip.user")
public class UserProperteis {

    private String name;

    private String age;
}

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserProperteis properteis;

    @Value("${user.name}")
    private String name;

    @GetMapping("name")
    public String getResult(){
        log.info("name - {}",name);
        return "success - " + name;
    }

    @GetMapping("user")
    public String getProperteis(){
        log.info("user properteis - {}",properteis);
        return properteis.toString();
    }
}
```


### 三、加解密多策略支持
> - `3.0.x`版本以上支持多种加解密策略，但目前无法支持动态刷新配置。`2.0.x`版本参考`apollo功能扩展.md`

- 
> [jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot)

- `PEM key as a resource location`
```yaml
jasypt:
    encryptor:
      privateKeyFormat: PEM
      privateKeyLocation: classpath:private_key.pem
```
- `PEM key as string`
```yaml
jasypt:
    encryptor:
      privateKeyFormat: PEM
      privateKeyString: |-
          -----BEGIN PRIVATE KEY-----
          MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCtB/IYK8E52CYM
          ZTpyIY9U0HqMewyKnRvSo6s+9VNIn/HSh9+MoBGiADa2MaPKvetS3CD3CgwGq/+L
          IQ1HQYGchRrSORizOcIp7KBx+Wc1riatV/tcpcuFLC1j6QJ7d2I+T7RA98Sx8X39
          orqlYFQVysTw/aTawX/yajx0UlTW3rNAY+ykeQ0CBHowtTxKM9nGcxLoQbvbYx1i
          G9JgAqye7TYejOpviOH+BpD8To2S8zcOSojIhixEfayay0gURv0IKJN2LP86wkpA
          uAbL+mohUq1qLeWdTEBrIRXjlnrWs1M66w0l/6JwaFnGOqEB6haMzE4JWZULYYpr
          2yKyoGCRAgMBAAECggEAQxURhs1v3D0wgx27ywO3zeoFmPEbq6G9Z6yMd5wk7cMU
          vcpvoNVuAKCUlY4pMjDvSvCM1znN78g/CnGF9FoxJb106Iu6R8HcxOQ4T/ehS+54
          kDvL999PSBIYhuOPUs62B/Jer9FfMJ2veuXb9sGh19EFCWlMwILEV/dX+MDyo1qQ
          aNzbzyyyaXP8XDBRDsvPL6fPxL4r6YHywfcPdBfTc71/cEPksG8ts6um8uAVYbLI
          DYcsWopjVZY/nUwsz49xBCyRcyPnlEUJedyF8HANfVEO2zlSyRshn/F+rrjD6aKB
          V/yVWfTEyTSxZrBPl4I4Tv89EG5CwuuGaSagxfQpAQKBgQDXEe7FqXSaGk9xzuPa
          zXy8okCX5pT6545EmqTP7/JtkMSBHh/xw8GPp+JfrEJEAJJl/ISbdsOAbU+9KAXu
          PmkicFKbodBtBa46wprGBQ8XkR4JQoBFj1SJf7Gj9ozmDycozO2Oy8a1QXKhHUPk
          bPQ0+w3efwoYdfE67ZodpFNhswKBgQDN9eaYrEL7YyD7951WiK0joq0BVBLK3rwO
          5+4g9IEEQjhP8jSo1DP+zS495t5ruuuuPsIeodA79jI8Ty+lpYqqCGJTE6muqLMJ
          Diy7KlMpe0NZjXrdSh6edywSz3YMX1eAP5U31pLk0itMDTf2idGcZfrtxTLrpRff
          umowdJ5qqwKBgF+XZ+JRHDN2aEM0atAQr1WEZGNfqG4Qx4o0lfaaNs1+H+knw5kI
          ohrAyvwtK1LgUjGkWChlVCXb8CoqBODMupwFAqKL/IDImpUhc/t5uiiGZqxE85B3
          UWK/7+vppNyIdaZL13a1mf9sNI/p2whHaQ+3WoW/P3R5z5uaifqM1EbDAoGAN584
          JnUnJcLwrnuBx1PkBmKxfFFbPeSHPzNNsSK3ERJdKOINbKbaX+7DlT4bRVbWvVj/
          jcw/c2Ia0QTFpmOdnivjefIuehffOgvU8rsMeIBsgOvfiZGx0TP3+CCFDfRVqjIB
          t3HAfAFyZfiP64nuzOERslL2XINafjZW5T0pZz8CgYAJ3UbEMbKdvIuK+uTl54R1
          Vt6FO9T5bgtHR4luPKoBv1ttvSC6BlalgxA0Ts/AQ9tCsUK2JxisUcVgMjxBVvG0
          lfq/EHpL0Wmn59SHvNwtHU2qx3Ne6M0nQtneCCfR78OcnqQ7+L+3YCMqYGJHNFSa
          rd+dewfKoPnWw0WyGFEWCg==
          -----END PRIVATE KEY-----
```


### 三、问题
> `apollo`集成`jasypt-spring-boot-starter`后，`@Value`注解参数无法自动刷新
> - 自动刷新类：`AutoUpdateConfigChangeListener`的`updateSpringValue`方法
```properties
1. apollo集成jasypt进行配置加密时，发现配置动态更新失效。不仅ConfigurationProperties类无法动态刷新，连@Value注解的参数也无法动态刷新。

原因
jasypt与apollo出现冲突所致，jasypt-spring-boot-starter版本仅支持到2.0。超过此版本则会导致apollo无法动态刷新。
```
* [关于Apollo配置中心 无法动态刷新任何配置文件的一些坑](https://blog.csdn.net/HiBoyljw/article/details/89243577)

### 五、参考文档

- `使用`相关
* ★★★[终于把Apollo存储加密这件事搞定了](https://www.cnblogs.com/yinjihuan/p/11112111.html)
* [spring boot 集成jasypt3.0.3](https://www.91mszl.com/zhangwuji/article/details/1364)
* [Jasypt加密工具整合SpringBoot使用](https://www.jianshu.com/p/c7125139d688)
* [Spring Boot + jasypt 对配置文件加密](https://www.cnblogs.com/dreamstar99/p/14363516.html)
* [springboot 自定义配置文件加密规则](https://cloud.tencent.com/developer/article/1816936)

- `自定义加解密`相关
---
* ★★★[jasypt-spring-boot-starter实现加解密和数据返显](https://blog.csdn.net/m0_37635053/article/details/118256179)
* ★★★[SpringBoot：整合jasypt ](https://www.cnblogs.com/wwjj4811/p/14738704.html)
* ★★[Spring Boot demo系列（九）：Jasypt](http://t.zoukankan.com/6b7b5fc3-p-13725721.html)
* ★★[SpringBoot使用jaspyt对配置文件加密](https://my.oschina.net/xiaozhiwen/blog/5083845)
* [spring boot使用jasypt加密原理解析](https://blog.csdn.net/u013905744/article/details/86508236)
* [@Value 值在引入jasypt后无法自动更新 #2170](https://github.com/apolloconfig/apollo/issues/2170)
* [JASYPT-SPRING-BOOT-STARTER 版本的一些坑，导致NACOS 自动刷新失败](https://www.freesion.com/article/27781440432/)

---
- `Apollo`相关
* ★★ [Apollo支持@ConfigurationProperties动态刷新](https://blog.csdn.net/luo15242208310/article/details/115521686)
* [springBoot实现配置和实例的热更新，集成Apollo，方法通用](https://blog.csdn.net/qq_32635069/article/details/83655625)
* [apollo配置中心实现自定义加密配置处理](https://www.jianshu.com/p/12b4c24c792e)
* [Apollo配置中心遇到的坑](https://www.jianshu.com/p/7d91cb5109a4)

---
- 代码示例
* [apollo-use-cases](https://github.com/sunnyvinson/apollo-use-cases)
* [配置中心-最佳实践](https://cloud.tencent.com/document/product/1364/58879)





