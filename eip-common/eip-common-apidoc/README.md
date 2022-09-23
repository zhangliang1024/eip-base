# `SpringBoot`整合`Swagger3`生成接口文档


### `Swagger`注解的使用说明
```text
@Api：用在请求的类上，表示对类的说明
    tags="说明该类的作用，可以在UI界面上看到的注解"
    value="该参数没什么意义，在UI界面上也看到，所以不需要配置"

@ApiOperation：用在请求的方法上，说明方法的用途、作用
    value="说明方法的用途、作用"
    notes="方法的备注说明"

@ApiImplicitParams：用在请求的方法上，表示一组参数说明
    @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
        name：参数名
        value：参数的汉字说明、解释
        required：参数是否必须传
        paramType：参数放在哪个地方
            · header --> 请求参数的获取：@RequestHeader
            · query --> 请求参数的获取：@RequestParam
            · path（用于restful接口）--> 请求参数的获取：@PathVariable
            · div（不常用）
            · form（不常用）    
        dataType：参数类型，默认String，其它值dataType="Integer"       
        defaultValue：参数的默认值

@ApiResponses：用在请求的方法上，表示一组响应
    @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
        code：数字，例如400
        message：信息，例如"请求参数没填好"
        response：抛出异常的类

@ApiModel：用于响应类上，表示一个返回响应数据的信息
            （这种一般用在post创建的时候，使用@RequestBody这样的场景，
            请求参数无法使用@ApiImplicitParam注解进行描述的时候）
    @ApiModelProperty：用在属性上，描述响应类的属性
```


### 项目集成
1. 依赖
```xml
 <dependency>
    <groupId>com.eip.cloud</groupId>
    <artifactId>eip-common-apidoc</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
2. 配置文件
```yaml
eip:
  api:
    doc:
      swagger:
        enabled: true
        group-name: business
        title: "昂立课堂(业务版)"
        version: 1.0.0
        contact:
          name: 朱增亮
          emial: zhangsan@163.com
          url: http://localhost:8888/doc.html#/home
        description: "昂立课堂第二版，在SDK版上升级了业务模块，业务模块包括：用户、老师、学生、课程、课节、上课统计流水等。"
        host: http://localhost:1111
knife4j:
  enable: true
  production: true #生产环境屏蔽所有swagger资源
  basic:
    enable: true   #开启账号密码验证
    username: admin
    password: admin
```


### 文档
>  Swagger3中加入Json Web Token

[Springboot学习(六)swagger使用说明](https://zhuanlan.zhihu.com/p/144754872?from_voters_page=true)
[Swagger3快速使用](https://blog.csdn.net/yeahPeng11/article/details/120938449)
[整合swagger、knife4j](https://www.cnblogs.com/mjtabu/p/14187853.html)
[Knife4j](https://gitee.com/xiaoym/knife4j)
[knife4j](https://doc.xiaominfo.com/knife4j/)


---

# `SpringBoot`整合`SpringDoc+Knife4j`
> 问题：`Springboot`升级2.6版本后，使用`Swagger3`启动会报错：`Failed to start bean ‘documentationPluginsBootstrapper’; nested exception is java.lang.NullPointerException`

### 一、依赖
> - `pom.xml`
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.11</version>
</dependency>
<!--引入Knife4j的官方ui包,OpenAPI3建议使用springdoc-openapi项目-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-springdoc-ui</artifactId>
    <version>3.0.3</version>    
</dependency>
```

### 二、配置
> - `application.yml`
```yaml
springdoc:
  swagger-ui:
    enabled: true #开启Swagger UI界面
#    path: /swagger-api/ #修改Swagger UI路径 http://localhost:8080/swagger-api/swagger-ui/index.html
#    disable-swagger-default-url: true
  api-docs:
    enabled: true #开启api-docs
    path: /v3/api-docs #修改api-docs路径
  packages-to-scan: com.eip.sample.springdoc.web #配置需要生成接口文档的包扫描
  paths-to-match: /system/**,/captcha/**,/login/** #配置需要生成接口文档的接口路径，哪些路径生效由这里的配置最终决定
  eip-docs:
    group-name: eip-sample
```

### 三、配置类
```java
@RequiredArgsConstructor
@EnableConfigurationProperties(ApidocInfo.class)
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true)
public class SpringDocConfig {

    private final ApidocInfo apidocInfo;

    @Bean
    public OpenAPI springShopOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .externalDocs(externalDocs())
                .addSecurityItem(securityItem())
                .components(components());
    }
    //...
}
```

### 四、结合`SpirngSecurity`使用
> 项目集成`SpringSecurity`，需要配置`SpirngDoc`白名单路径
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {                                              

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // Swagger的资源路径需要允许访问
                        "/",   
                        "/swagger-ui.html",
                        "/swagger-ui/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v3/api-docs/**"
                )
                .permitAll()
                .antMatchers("/admin/login")// 对登录注册要允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        
    }
}
```


### 参考文档
[SpringBoot集成SpringDoc](https://blog.csdn.net/tenyears940326/article/details/126467823)
[SpringBoot的API文档生成工具SpringDoc使用详解](https://www.jb51.net/article/252272.htm)
[神器 SpringDoc 横空出世，最适合 SpringBoot 的API文档工具来了](https://blog.csdn.net/zhenghongcs/article/details/123812583)
[升级Springboot2.6后使用Swagger3报错](https://blog.csdn.net/qq_40977118/article/details/124387411)