# 灰度组件
> 灰度组件，默认支持基于header头的灰度策略。支持自定义扩展，可根据实际情况获取灰度标识，如：`token` `cache` `msyql` 等。
> 也可基于此，实现一套链路管理工具。从注册中心获取所有服务，然后打标识，进行**灰度链路管理**。

## 一、灰度标识配置
> 基于 `eureka` 的元数据配置
```yml
eureka:
  instance:
    metadata-map:
      tagName1 : tagValue1
      tagName2 : tagValue2
```
> 基于 `nacos` 的元数据配置
```yml
spring:
  application:
    name: eip-sample-nacos
  cloud:
    nacos:
      server-addr: 140.246.xxx.99:8848
      discovery:
        metadata:
          group: ${spring.application.name}
          version: 1.0
```


## 二、实现原理
> `Ribbon` 可以根据客户端注册在 `eureka` 上的 `tag` 实现动态路由

### 核心源码
> `RibbonFilterContextHolder`，在 `zuul` 转发或 `feign` 调用前写入动态路由策略，指定转发到有该tagValue的服务

```java
RibbonFilterContextHolder.getCurrentContext().add("${tagName}","${tagValue}");
```

> `GrayServiceHandler` 支持扩展自定义访问策略，默认实现从header获取 `tag` : `RequestHeaderGrayServiceHandler`
```java
public abstract class GrayServiceHandler {

    public static final String GRAY_VERSION = "grayVersion";

    protected boolean handle(String serviceId) {
        String grayVersion = getGrayVersion(serviceId);
        if (StringUtils.isNotBlank(grayVersion)) {
            RibbonFilterContextHolder.clearCurrentContext();
            RibbonFilterContextHolder.getCurrentContext().add(GRAY_VERSION, grayVersion);
            return true;
        }
        return false;
    }

    /**
     * 支持通过子类实现来扩展自定义访问策略
     */
    protected abstract String getGrayVersion(String serviceId);
}
```

> `FeignRibbonFilterAspect` 拦截 `FeignClient` 在调用前指定调用 `tag` ，调用后清除


## 三、案例演示

- 启动项目
> 启动 `eureka` `zuul` `serviceA` `serviceB` `serviceC` `serviceD` (通过指定 `-Dspring.profiles.active=dev1\dev2`) ，来加载不同配置文件模拟不同灰度服务

- 接口
> GET http://localhost:8092/service-b/demo/hello
> 该接口会经由 `zuul`->`serviceB`->`serviceC`->`serviceD`->`serviceA` ，将打印各个服务的 `tag` 信息，可观测到每次请求在不同版本的服务中轮询

- `header`添加

header头|header值
---|---
ribbon-lancher-map | {"service-a":"green","service-b":"blue","service-c":"green","service-d":"green"}

- 示例图

<img src="https://pic5.58cdn.com.cn/nowater/webim/big/n_v24819cca4fd6245e4a4a9f830251f38c4.png" alt="企业微信截图_16729742619056.png" title="企业微信截图_16729742619056.png" />

- 日志
```shell
2023-01-06 14:38:37.836  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] ============= CHOOSE SERVER BEGIN =============
2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] Discovery server: [service-b:192.168.120.131:8084] metadata: [{grayVersion=blue, management.port=8084}]
2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] Ribbon grayVersion: [blue]
2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] Ribbon choose server: [service-b-192.168.120.131:8084] grayVersion: [blue]
2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] ============= CHOOSE SERVER END =============

2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] ============= CHOOSE SERVER BEGIN =============
2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] Discovery server: [service-b:192.168.120.131:8083] metadata: [{grayVersion=green, management.port=8083}]
2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] Ribbon grayVersion: [blue]
2023-01-06 14:38:37.837  INFO 115496 --- [nio-8092-exec-3] c.e.c.g.e.p.EurekaMetadataAwarePredicate : [GRAY-RIBBON] ============= CHOOSE SERVER END =============
```
- 结论
> 可以观测到每次调用链与 `header` 中指定的一致


