# 定时任务组件
> 基于`xxl-job`的`2.3.1-SNAPSHOT`改造而来
>
> 任务调度中心登录：`admin\123456`，建立执行器和任务策略。 

## 一、添加依赖
> - `pom.xml` 如果按标准建立项目则无需依赖。
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
eip:
  job:
    enabled : true
    port: 30006
    adminAddresses: http://192.168.207.6:9006/job-admin
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
    public void execute(String param) throws Exception {
        log.info("jobHandler 9006 ProviderApplication start!");
        XxlJobHelper.log("[xxl-job] execute params : {}" ,params);
    }
}
```

