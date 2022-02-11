# 日志组件


### 移除`logback`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>spring-boot-starter-logging</artifactId>
            <groupId>org.springframework.boot</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

### 配置使用
```yaml
logging:
  file:
    name: eureka-server
    path: logs
```


### `log4j2`多环境配置文件加载多次
> `log4j2-test.xml`其中`test`为保留字
> - [springboot2 log4j2 加载两次配置文件加载顺序](https://www.jianshu.com/p/1de41185bff3)

![](https://ae03.alicdn.com/kf/H962dc54dd39b485985069f2bd2d192bdV.png)
