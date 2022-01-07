# file-upload-spring-boot-starter
> Springboot集成阿里云、七牛、腾讯云、本地 文件上传。支持扩展接入类型。
>
> 说明：采用自定义starter的方式，通过IOC扫描方式实现各云存储实现类的初始化注入，通过策略模式+工厂模式构建唯一的对象来进行业务实现。
>
> [项目演示demo:](https://github.com/zhangliang1024/file-upload-spring-boot-sample)

### 一、接入方式
> pom.xml
```xml
<dependency>
    <groupId>com.zhliang.pzy</groupId>
    <artifactId>file-upload-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
> 项目入口类
```java
@EnableFileupLoad
@SpringBootApplication(scanBasePackages = {"com.zhliang.pzy.file"})
```
> 配置文件
```yaml
spring:
  cloud:
    store:
      type: LOCAL
      local-prefix: D:/photo
```

### 二、接入方式扩展
```markdown
1. 实现 IFileType 接口扩展接入存储类型
2. 实现 ICloudStorageService 接口实现上传方式
   
```

### 三、后期功能
```markdown
1. 加入下载、删除、预览功能
2. 数据加密传输
3. 对上传格式做白名单限制
4. 对参数配置进行校验
```

