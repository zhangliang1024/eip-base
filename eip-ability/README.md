# 架构功能说明


## `eip-ability-log`
- 包结构示例
```properties
# 业务名称
business 
  # 数据库操作
  mapper 
    BusinessMapper.java
    BusinessMapper.xml
  # 数据对象
  model 
    BusinessDTO.java
  # 业务逻辑
  service
    IBusinessService.java
    BusinessServiceImpl.java
  # 业务入口
  BusinessController.java
```
- `pom.xml`中加入扫描`mapper.xml`配置
```xml
<build>
    <resources>
        <!--mapper.xml放入java包结构中，需加配置-->
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
        <!-- 替换勾选的环境 -->
        <resource>
            <filtering>true</filtering>
            <directory>src/main/resources</directory>
            <includes>
                <include>META-INF/**</include>
                <include>**/*.xml</include>
                <include>**/banner.txt</include>
                <include>bootstrap.yml</include>
                <include>bootstrap-${my-profile}.yml</include>
                <include>application.yml</include>
                <include>application-${my-profile}.yml</include>
            </includes>
        </resource>
    </resources>
</build>
```
- `Api`返回结果
> 通过`GlobalResponseAdvice`统一处理返回结果
- 代码示例
```java
public class LogController{
    @GetMapping("/success")
    public LogDTO testSuccess() throws Exception {
        LogDTO log = new LogDTO();
        log.setId(1L);
        log.setStr("str");
        log.setList(Arrays.asList("1","2","3"));
        return log;
    }
}
```
- 最终结果
```json
{
    "code": "200",
    "message": "success",
    "logTraceId": "",
    "data": {
        "id": 1,
        "str": "str",
        "list": [
            "1",
            "2",
            "3"
        ]
    }
}
```
- `@MapperScan`与`Mapper`
> 问题：`@MapperScan`扫描包里面混有`@Service`时，会把`service`也做为对象进行注入
> - 即无法正确区分哪些是真正的`dao`接口
```properties
1. 方法一：合理划分包结构，service移动到其它包结构
2. 方法二：不使用@MapperScan注解，在每个dao上加@Mapper注解即可
3. 方法三：自定义扫描注解
```
[解决@MapperScan扫描包里面混有@Service](https://blog.csdn.net/weixin_43328357/article/details/103993232)
