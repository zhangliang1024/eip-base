# xss-spring-boot-starter
> 在项目开发中系统会存在XSS漏洞，JAVA后端防止XSS攻击（SQL注入、HTML、SCRIPT）基础方法

### 一、说明


### 二、使用
> pom 配置
```xml
<dependency>
    <groupId>com.zhliang.pzy</groupId>
    <artifactId>xss-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
> yaml 配置
```yaml
pzy:
  xss:
    filter:
      # 是否开启，默认true
      enabled: true
      # 加载顺序 默认：0
      order: -100
      # 过滤器名称 默认：xssFilter
      name: xxsFilterName
      # 过滤路径 默认：/*
      url-patterns:
        - "/limit"
```
