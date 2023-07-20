# `RocketMQ` 自定义扩展

## 一、解决不支持`LocalDate`和`LocalDateTime`
```text
rocketmq-spring-boot-starter 内置的消息转换器是 RocketMQMessageConverter，不支持LocalDate时间类型的转换。

自定义消息转换器，支持时间模块
```

## 二、实现`Topic`环境级别灰度隔离
- 问题
```text
在日常开发中，通常会在代码中指定消息主题（topic）。当我们在开发测试环境共用一个RocketMQ环境时。
会存在开发环境的消息被测试环境消费，或者开发环境的消息没有在本机消费，而跑到其它环境或节点的情况，不利于业务验证测试。
```
- 方案
```text
组件将实现根据不同的环境和灰度标识，实现消息的消费隔离。
```

## 三、统一消息发送接受工具
### 1、背景介绍
```text
1. 在项目实践中，一个完整的消息传递链路从生产->到消费包括:
   准备消息->发送消息->记录发送消息日志->处理发送失败->记录接受消息日志->处理业务逻辑->异常处理和重试
   
2. 通过封装实现，生产者只需要准备好消息实体并调用封装后的工具进行发送，消费者只需要处理核心业务逻辑。其它
   公共逻辑由组件统一处理。
```

### 2、核心类介绍
```text
BaseMessage: 消息实体封装
RocketMQEnhanceTemplate: 消息发送工具封装，实现了日志记录、自动重建topic功能（实现环境隔离、灰度topic）
EnhanceMessageHandler: 消费者核心业务封装，使用模板设计模式定义了消费消息的骨架，实现了日志打印、异常处理、异常重试、消息过滤等，业务处理则交由子类实现

RocketMQEnhanceAutoConfiguration：基础配置类，初始化Bean，实现环境隔离配置类、增加Template、支持LocalDate等
RocketEnhanceProperties: 配置类
```

## 四、示例代码
- `application.yaml`
```yaml
rocketmq:
  consumer:
    group: springboot_consumer_group
    # 一次拉取消息最大值，注意是拉取消息的最大值而非消费最大值
    pull-batch-size: 10
  name-server: 140.xxx.xxx.99:9876
  producer:
    # 发送同一类消息的设置为同一个group，保证唯一
    group: springboot_producer_group
    # 发送消息超时时间，默认3000
    sendMessageTimeout: 10000
    # 发送消息失败重试次数，默认2
    retryTimesWhenSendFailed: 2
    # 异步消息重试此处，默认2
    retryTimesWhenSendAsyncFailed: 2
    # 消息最大长度，默认1024 * 1024 * 4(默认4M)
    maxMessageSize: 4096
    # 压缩消息阈值，默认4k(1024 * 4)
    compressMessageBodyThreshold: 4096
    # 是否在内部发送失败时重试另一个broker，默认false
    retryNextServer: false
  enhance:
    # 启动隔离，用于激活配置类EnvironmentIsolationConfig
    # 启动后会自动在topic上拼接激活的配置文件，达到自动隔离的效果
    enabledIsolation: true
    # 隔离环境名称，拼接到topic后，topic_dev，默认空字符串
    environment: dev
    # 灰度标识，拼接到topic后，topic_dev_gray，默认空字符串
    gray-flag: gray
```
- 生产者
```java
@Data
public class MemberMessage extends BaseMessage {


    private String userName;
    private LocalDate birthday;
}
@RestController
@RequestMapping("enhance")
@Slf4j
public class EnhanceProduceController {
    // 注入增强后的模板，可以自动实现环境隔离，日志记录
    @Setter(onMethod_ = @Autowired)
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    private static final String topic = "rocket_enhance";
    private static final String tag = "member";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /**
     * 发送实体消息
     */
    @GetMapping("/member")
    public SendResult member() {
        String key = UUID.randomUUID().toString();
        MemberMessage message = new MemberMessage();
        // 设置业务key
        message.setKey(key);
        // 设置消息来源，便于查询
        message.setSource("MEMBER");
        // 业务消息内容
        message.setUserName("Java日知录");
        message.setBirthday(LocalDate.now());
        return rocketMQEnhanceTemplate.send(topic, tag, message);
    }
}
```
- 消费者
```java
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "enhance_consumer_group",
        topic = "rocket_enhance",
        selectorExpression = "*",
        consumeThreadMax = 5 // 默认是64个线程并发消息，配置 consumeThreadMax 参数指定并发消费线程数，避免太大导致资源不够
)
public class EnhanceMemberMessageListener extends EnhanceMessageHandler<MemberMessage> implements RocketMQListener<MemberMessage> {
    @Override
    protected void handleMessage(MemberMessage message) throws Exception {
        // 此时这里才是最终的业务处理，代码只需要处理资源类关闭异常，其他的可以交给父类重试
        System.out.println("业务消息处理:" + message.getUserName());
    }

    @Override
    protected void handleMaxRetriesExceeded(MemberMessage message) {
        // 当超过指定重试次数消息时此处方法会被调用
        // 生产中可以进行回退或其他业务操作
        log.error("消息消费失败，请执行后续处理");
    }

    /**
     * 是否执行重试机制
     */
    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean throwException() {
        // 是否抛出异常，false搭配retry自行处理异常
        return false;
    }

    @Override
    protected boolean filter(MemberMessage message) {
        // 消息过滤
        return false;
    }

    /**
     * 监听消费消息，不需要执行业务处理，委派给父类做基础操作，父类做完基础操作后会调用子类的实际处理类型
     */
    @Override
    public void onMessage(MemberMessage memberMessage) {
        super.dispatchMessage(memberMessage);
    }
}
```
- 消息日志
```shell
2023-07-20 16:53:19.086  INFO 62284 --- [nio-6006-exec-2] c.e.c.b.r.t.RocketMQEnhanceTemplate      : [rocket_enhance_dev:member]同步消息[{"birthday":"2023-07-20","retryTimes":0,"source":"MEMBER","userName":"Java日知录","key":"edd5b21e-3c57-40b1-a2c5-13834e52042f","sendTime":"2023-07-20T16:53:17.132"}]发送结果[{"traceOn":true,"regionId":"DefaultRegion","messageQueue":{"queueId":2,"topic":"rocket_enhance_dev","brokerName":"broker-a"},"msgId":"7F000001F34C18B4AAC265788C130000","queueOffset":0,"sendStatus":"SEND_OK","offsetMsgId":"8CF69A6300002A9F000000000000183B"}]
2023-07-20 16:53:19.111  INFO 62284 --- [onsumer_group_1] c.e.c.b.r.handler.EnhanceMessageHandler  : 消费者收到消息[{"birthday":"2023-07-20","retryTimes":0,"source":"MEMBER","userName":"Java日知录","key":"edd5b21e-3c57-40b1-a2c5-13834e52042f","sendTime":"2023-07-20T16:53:17.132"}]
业务消息处理:Java日知录
2023-07-20 16:53:19.111  INFO 62284 --- [onsumer_group_1] c.e.c.b.r.handler.EnhanceMessageHandler  : 消息edd5b21e-3c57-40b1-a2c5-13834e52042f消费成功，耗时[0ms]
```

- 支持根据业务键查询消息

![企业微信截图_16898468653339.png](https://pic1.58cdn.com.cn/nowater/webim/big/n_v2d7c81e46fd8b4e9fad6b321739128c73.png)

---
## 五、参考文档
[SpringBoot整合RocketMQ，老鸟们都是这么玩的](https://developer.aliyun.com/article/1256771)
[]()
[]()
[]()