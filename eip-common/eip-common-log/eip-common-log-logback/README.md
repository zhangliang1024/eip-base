# `logback`记录日志
> `springcloud`项目用`bootstrap.yml`文件

### 配置使用
```yaml
logging:
  file:
    name: ${spring.application.name}
    path: logs
```


### `logback-spring.xml`配置
- `springboot`项目
> `springboot`项目中，可以直接使用`springProperty`来引用`application.yml`中的配置

- `springcloud`项目
> `springcloud`项目中，因为`spring cloud`会创建一个`Bootstrap context`，来做为`spring`应用的`application context`的父上下文。此时就会用到`log`配置
> 故若此时在日志配置中，需要从配置文件取参数，需要放在`bootstrap.yml`中，放可取到。

![](https://ae01.alicdn.com/kf/Hce786495bd7f4239a1aa60f0fe90f18eG.png)


- `spring.log`生产原因

![](https://ae05.alicdn.com/kf/Hf0dccd5c52de47309a9059d2338c1271d.png)


### `xxx_IS_UNDEFINED`说明
> 日志路径或文件出现此情况，怎说明：`logback`比`springboot`先加载，找不到变量值，默认为此值
> - 除了上述方式，使用`bootstrap.yml`配置
> - 将`logback-spirng.xml`中变量通过，系统属性方式。在启动项目时指定

### 参考文档
* [如何在logback.xml中自定义动态属性 ](https://www.cnblogs.com/spec-dog/p/11386668.html)
* [springboot超级详细的日志配置(基于logback)](https://cloud.tencent.com/developer/article/1445599)
* [MDC日志链路设计](https://www.cnblogs.com/zdd-java/p/15630210.html)
* []()

- 性能
* [Logback 配置文件这样优化，TPS提高 10 倍](https://mp.weixin.qq.com/s/GqW92DVZTaMHQnsWCBjFhQ)