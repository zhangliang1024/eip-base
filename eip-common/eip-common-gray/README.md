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



## 四、示例代码
[eip-sample-gray](https://github.com/zhangliang1024/eip-base/tree/master/eip-sample/eip-sample-gray)

## `Ribbon`负载均衡
> `Ribbon`是一个内置软件`负载均衡器`的进程间通信(远程过程调用)库。`Ribbon`客户端组件提供一系列完善的配置项如连接超时，重试等。

- `Ribbon`是一个经过云测试的客户端库。提供了以下特性
> - Load balancing 负载均衡
> - Fault tolerance 故障容错
> - 异步和响应模型中支持多个协议(HTTP\TCP\UDP)
> - 缓存和批处理

- `Ribbon`核心组件

组件名称|组件说明
---|---
ServerList | 响应客户端的特定服务的服务器列表
ServerListFilter | 动态获得的具有所需特征的候选服务器列表的过滤器
ServerListUpdate | 用于执行动态服务器列表更新
IRule | 负载均衡策略，用于确定从服务器列表返回哪个服务器
IPing | 客户端用于快速检查服务器当时是否处于活动状态（心跳检测）
ILoadBalancer | 负载均衡器，负责负载均衡调度的管理
IClientConfig | 定义Ribbon中管理配置的接口 

<img src="https://pic8.58cdn.com.cn/nowater/webim/big/n_v265b6e9ad98f349ba87a7a92cf2ad989c.png" alt="企业微信截图_1673322150657.png" title="企业微信截图_1673322150657.png" />



- 路由规则器：`PredicateBaseRule`
<img src="https://pic5.58cdn.com.cn/nowater/webim/big/n_v27f26887760064792abe59597a625a426.png"/>
```java
// 抽象策略，继承自ClientConfigEnabledRoundRobinRule
// 基于Predicate策略 Predicate是Google Guava Collection工具对集合进行过滤的条件接口
public abstract class PredicateBasedRule extends ClientConfigEnabledRoundRobinRule {
    public PredicateBasedRule() {
    }

    // 定义一个抽象函数来获取 AbstractServerPredicate
    public abstract AbstractServerPredicate getPredicate();

    public Server choose(Object key) {
        ILoadBalancer lb = this.getLoadBalancer();
        // 通过AbstractServerPredicate的chooseRoundRobinAfterFiltering函数来选出具体的服务实例
        // AbstractServerPredicate的子类实现的Predicate逻辑来过滤一部分服务实例
        // 然后在以线性轮询的方式从过滤后的实例中选出一个
        Optional<Server> server = this.getPredicate().chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
        return server.isPresent() ? (Server)server.get() : null;
    }
}
```
<img src="https://pic6.58cdn.com.cn/nowater/webim/big/n_v24f6e800d2a7f41169e9336a0cf6942dd.png"/>
