<meta name="referrer" content="no-referrer" />
# 定时任务组件
> 基于`xxl-job`的`2.3.1-SNAPSHOT`改造而来
>
> 任务调度中心登录：`admin\123456`，建立执行器和任务策略。 

## 一、添加依赖
> `pom.xml` 如果按标准建立项目则无需依赖。
```xml
<dependency>
    <groupId>com.eip.cloud</groupId>
    <artifactId>eip-common-job-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

---
# 二、组件使用
- 配置文件
> - `AppName` : 填项目名称 `${spring.application.name}`
> - `机器地址`: 填项目机器ip 
```yml
spring:
  application:
    name: eip-sample-job
eip:
  job:
    enabled : true
    port: 30006
    adminAddresses: http://localhost:9006/job-admin
    username: admin
    password: 123456
    title: eip-sample

```

- 代码
> 需要继承`IJobHandler` 
```java
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import com.sgcc.eip.cmc.suite.common.log.Logger;
import com.sgcc.eip.cmc.suite.common.log.LoggerFactory;

@Slf4j
@Component
public class JobHandler extends IJobHandler {

    @XxlJob(value = "jobHandler")
    public void execute() throws Exception {
        log.info("jobHandler 9006-execute start!");
        XxlJobHelper.log("[xxl-job] execute finished.");
    }

    @XxlJob(value = "testJob")
    @XxlAutoRegister(cron = "0 0 0 * * ? *", author = "eip-zhliang", jobDesc = "测试job")
    public void testJob() {
        log.info("--------#公众号：码农参上--------");
        XxlJobHelper.log("[xxl-job] testJob execute finished");
    }


    @XxlJob(value = "testJob222")
    @XxlAutoRegister(cron = "0/3 * * * * ?", triggerStatus = 1)
    public void testJob2() {
        log.info("--------#作者：Hydra--------");
        XxlJobHelper.log("[xxl-job] testJob222 execute finished");
    }
}
```

## 四、示例图
> 执行器管理

<img src="http://tva1.sinaimg.cn/large/d1b93a20ly1h9afdut86pj21h90mb44f.jpg"/>

> 任务管理

<img src="http://tva1.sinaimg.cn/large/d1b93a20ly1h9afeozeeaj21h90mp7bm.jpg"/>

## 五、原理图
> 自动注册执行器和任务得原理：在项目启动时主动注册`executor`和各个`jobHandler`到调度中心。 

<img src="http://tva1.sinaimg.cn/large/d1b93a20ly1h9afhsvcvhj20r908477q.jpg"/>

## 六、参考文档
[Xxl-Job告别手动配置任务](https://www.1024sou.com/article/1047399.html)