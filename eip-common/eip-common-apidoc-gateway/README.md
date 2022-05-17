# SpringCloud Gateway Swagger聚合接口文档

### 一、使用说明
> 加入`pom`依赖
```xml
<dependency>
    <groupId>com.eip.cloud</groupId>
    <artifactId>eip-common-apidoc-gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
> `application.yml`配置
```yaml
spring:
  main:
    allow-bean-definition-overriding: true
  application:
    name: oauth2-cloud-gateway
  cloud:
    gateway:
      enabled: true
      # 当service实例不存在时默认返回503，显示配置返回404
      loadbalancer:
        use404: true
      # 效果：/serviceId/path1/args ==> /path1/arg
      discovery:
        locator:
          enabled: true
          lowerCaseServiceId: true
      ## 路由
      routes:
      ## id只要唯一即可，名称任意
      - id: oauth2-cloud-auth-server
        uri: http://localhost:3002
        predicates:
        ## Path Route Predicate Factory断言
        - Path=/oauth/**
      - id: oauth2-cloud-order
        uri: http://localhost:9002
        predicates:
        ## Path Route Predicate Factory断言
        - Path=/resources/**
        filters:
          - StripPrefix=1
          
eip:
  gateway:
    swagger:
      enabled: true
      api-docs: /v3/api-docs          
```

### 三、参考文档
* [Springcloud之gateway配置及swagger集成](https://www.cnblogs.com/pigmen/p/14092311.html)
* [SpringCloud Gateway整合Springfox Swagger 3.0](https://blog.csdn.net/erik_tse/article/details/116652064)
* [Springcloud-Gateway整合swagger3](https://blog.csdn.net/qq_43653951/article/details/117112553)
* [网关聚合服务Swagger文档](https://github.com/a852203465/swagger-gateway-spring-boot-starter)
