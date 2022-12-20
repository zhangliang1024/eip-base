# api-encrypt-spring-boot-starter
> 基于注解+AOP实现 API接口的加解密操作。采用RS非对象加解密方式
[演示demo](https://github.com/zhangliang1024/spring-fox-spring-boot-sample/tree/master/src/main/java/com/zhliang/pzy/spring/fox/encrypt)
---


### 二、使用
> pom.xml
```xml
 <dependency>
            <groupId>com.pzy.zhliang</groupId>
            <artifactId>api-encrypt-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
> 代码
```java
//启动类加注解：@EnableSecurity
@EnableSecurity
@SpringBootApplication
public class SpringFoxSpringBootSampleApplication {
    
}

@RestController
public class EncryptController {
    @Encrypt
	@PostMapping("/encrypt")
	public UserDto encryptEntity() {
		UserDto dto = new UserDto();
		dto.setId(1);
		dto.setName("加密实体对象");
		return dto;
	}
	
	@Encrypt
	@Decrypt
	@PostMapping("/save")
	public UserDto save(@RequestBody UserDto dto) {
		System.err.println(dto.getId() + "\t" + dto.getName());
		return dto;
	}
}
```
> application.yaml
```yaml
pzy:
  encrypt:
    rsa:
      open: true # 是否开启加密 默认：true  
      showLog: true # 是否打印加解密log 默认: false
      debug: false  # 是否调试模式，调试模式不进行加解密操作 默认：false
      publicKey: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMv5INQ/CdrB8rN1BSZTeOyqRO+ELTDb2SfxXN2tji+tFUlP1cCm4wFiOPybg4TA8BltX0qOXy8O49qdMlzUcs1KWg6Pu1iecTgUy+hKOyDYCmJx77e5QMcqfIsf2XSbmDbYleWKSl4TLzb0Djv1/dKcEWJC5h7kp8uwF55xkO5wIDAQAB
      privateKey: MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIy/kg1D8J2sHys3UFJlN47KpE74QtMNvZJ/Fc3a2OL60VSU/VwKbjAWI4/JuDhMDwGW1fSo5fLw7j2p0yXNRyzUpaDo+7WJ5xOBTL6Eo7INgKYnHvt7lAxyp8ix/ZdJuYNtiV5YpKXhMvNvQOO/X90pwRYkLmHuSny7AXnnGQ7nAgMBAAECgYBid9wcP73k8XhU6bveCUYXNhpBzVojAefwx8xfWo6GLeepfxrJ+9oe9j4m6H9jk4KkH/zL0CYgEwL6TQeV2MnDvxwEsOpgr1K0ynAA3et7qfsgSpx1LOaVyRnF8kNGV8Grz551Vj3QZ/aUMDawwnUpw72IdnfbiFv7flFVnBTY0QJBAMzGEoXvCEkbaRavmvbafZv7skA7une/snT9M9E6hz07u8PyR6GRbZMmetoopp/9Sbt9WgYpP1Zh8i5g+ImgSmsCQQCv9T5LBg6bfCVbaUuK8pdt61FWHdNFUDAjqI30TuyQ4qnlRoaXhbXEAoJkxVSlDXu37M+aeQktKsuBVSm7ICR1AkEAvRUMl5WPfgoMcIE4Q0afA8BW6M7+MILXIwWGqmUz6YvVpmp6UCs8FctLV1R//21ffvuRe+zsajRI7yBlfpd+/wJANIK1smH+XfxwkZUczDyVYGJHhbGk6RyDIUEeC/i5tItQNHIVnIZ0tighjb0uA4vAjiUH14ujZ21MCi7GI2f2OQJAD0qKInasT7YoJuZ9goyA9XDCoIDYj3J1YSsxn492tjNa5oSE6jO5p8uOC/W2cr3CVNIjys8E244NwXMQNS95ew==
```


### 三、参考项目
[前后端API交互数据加密——AES与RSA混合加密完整实例](https://www.cnblogs.com/huanzi-qch/p/10913636.html)

[前后端API交互如何保证数据安全性？](https://blog.csdn.net/ityouknow/article/details/80603617)

[monkey-api-encrypt](https://github.com/yinjihuan/monkey-api-encrypt)

[rsa-encrypt-body-spring-boot](https://github.com/ishuibo/rsa-encrypt-body-spring-boot)

[SpringBoot 接口加密解密，新姿势](https://blog.csdn.net/qq_42914528/article/details/128168527)

### 四、对称加密&非对称加密

- 对称加密
> 加密和解密都 使用同一秘钥算法

- 非对称加密
> 加密和解密 使用不同的秘钥

