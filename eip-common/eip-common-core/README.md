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