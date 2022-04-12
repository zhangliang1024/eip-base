# common-spring-boot-starter
> - 日常开发中，我们经常会对参数及返回结果等进行各种判断。根据判断的结果执行不同的逻辑，有些会抛出异常。可以说我们开发中一半的时间都在处理各种判断和异常情况。这样会导致我们的代码中充斥着大量的 `if...else ` 判断，及`try...catch`异常的捕获等逻辑，不仅代码大量冗余，且影响代码可读性。随着时间累积会导致代码结构混乱且日益膨胀逐渐难以维护。
>
> - 基于上面的这些原因。为了可以让我们把大量的精力投入到业务代码的开发中，这里封装了基础`common`组件，通过`Assert`+`ExceptionHandler`+`自定义Enum`类来简化对异常的处理



### 一、使用说明
> `pom.xml`
```xml
<dependency>
    <groupId>com.zhliang.pzy</groupId>
    <artifactId>common-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
> `application.properties`: 
```properties
# 在统一异常中，针对开发、测试和生产环境做了不同的返回实现。这里指定环境
spring.profiles.active=dev
```
> 业务实现
```java
@Transactional
@Override
public void updateSysUser(SysUserRequest sysUserRequest, Long id) {
    //查询修改后的用户名是否与其他用户冲突
    LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper();
    queryWrapper.eq(SysUser::getUsername, sysUserRequest.getUsername());
    queryWrapper.ne(SysUser::getId, id);
    int count = this.count(queryWrapper);
    //如果查询出来的列表数量大于0，说明冲突
    SysUserResponseEnum.USER_ALREADY_EXISTS.assertIsFalse(count > 0);
    SysUser sysUser = requestToEntity(sysUserRequest);
    sysUser.setId(id);
    this.updateById(sysUser);
}

@Transactional
@Override
public void updateSysUserState(Long id, Integer state) {
    //判断传入的用户标识是否正确，错误抛出异常
    SysUserStateEnum sysUserStateEnum = SysUserStateEnum.parseOfNullable(state);
    SysUserResponseEnum.USER_STATE_ERROR.assertNotNull(sysUserStateEnum);
    SysUser sysUser = new SysUser();
    sysUser.setId(id);
    sysUser.setState(state);
    this.updateById(sysUser);
}

```

## 三、集成`okhttp`
- `application.yml`
```yaml
ok.http.connect-timeout=30
ok.http.keep-alive-duration=300
ok.http.max-idle-connections=200
ok.http.read-timeout=30
ok.http.write-timeout=30
```
- 代码
```java
@Component
public class Business{
    
    @Autowired
    private OkHttpService httpService;
    
    public void send(){
        httpService.doGet("http://www.baidu.com");
    }
}

```

### 二、参考文档

[干掉 try catch](https://mp.weixin.qq.com/s/zdwHINfGng5ffv8L46iETw)
[干掉 try catch ！](https://blog.csdn.net/xcbeyond/article/details/105872632)
[Java生鲜电商平台-统一异常处理及架构实战](https://www.cnblogs.com/jurendage/p/11255197.html)
[Java生鲜电商平台-缓存架构实战](https://www.cnblogs.com/jurendage/p/11269241.html)
★★★ 好像是原作者 
[统一异常处理介绍及实战](https://www.jianshu.com/p/3f3d9e8d1efa)



[全局异常add-mappings配置false请求swagger-ui页面后台报错问题](https://blog.csdn.net/weixin_43241706/article/details/112303307)
[spring boot 异常(exception)处理 ](https://www.cnblogs.com/haibianren/p/11720502.html)
[静态资源的访问规则#](https://www.cnblogs.com/yoshi/p/14346943.html)

#### `全局ID`
* [基于Spring Boot的可直接运行的分布式ID生成器的实现以及SnowFlake算法详解](https://www.cnblogs.com/csonezp/p/12088432.html)
* [使用雪花算法为分布式下全局ID、订单号等简单解决方案考虑到时钟回拨](https://blog.csdn.net/ycb1689/article/details/89331634)
* [关于分布式唯一ID，snowflake的一些思考及改进(完美解决时钟回拨问题)](https://blog.csdn.net/WGH100817/article/details/101719325)

#### `okhttp`
* [Java封装OkHttp3工具类，用着贼舒服](https://blog.csdn.net/lilizhou2008/article/details/117537867)
* [OkHttpClient连接池问题](https://www.jianshu.com/p/e23c113f97f6)
* [再见，HttpClient！再见，Okhttp！](https://www.cnblogs.com/javastack/archive/2021/03/08/14501674.html)
* ★★★ [基于springboot的RestTemplate、okhttp和HttpClient对比](https://www.cnblogs.com/wzk-0000/p/10955406.html)
* [RestTemplate + okhttp3 简单使用](https://www.jianshu.com/p/e4fded8e126a)

---
## `okhttp`
> `OkHttp`是一个高效的`HTTP`客户端，允许所有同一主机地址的请求共享同一`socket`连接；连接池减少请求延时;

--- 
## `Logback`
> `logback`提供两种类型的过滤器，一种是`常规过滤器`，另一种是`全局过滤器`。`常规过滤器`与`appender`绑定，`全局过滤器`与`logger context`绑定。
> - `全局过滤器`过滤所有`logging request`，`常规过滤器`只过滤某个`appender`的`logging request`。

* [Logback 日志过滤器的使用-日志分类归档方案](https://blog.csdn.net/weixin_45505313/article/details/107382558)
* [java基础教程栏目介绍如何解决Java日志级别等问题](https://www.yht7.com/news/126652)


--- 
## 数据脱密
> {
>      "name":"L****",
>      "idNum":"1234************89",
>      "phone":"130****6666",
>      "address":"深圳市##########01",
>      "password":"a******we"
> }
* [Spring Boot 自定义注解之脱敏注解](https://juejin.cn/post/7022455226984726535)