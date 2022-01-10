# Web组件


### 三、接口多版本
- 核心类
> - `ApiVersionCondition`
> - `ApiVersionHandlerMapping`

```java

```

- 使用方式
```java
@ApiVersion(1.0)
@RestController
public class UserController{
    
    
    @ApiVersion(1.0)
    @GetMapping("user/{id}")
    public void getUser(@PathVAriable(id) Long id){
        //....
    }
    
    @ApiVersion(2.0)
    @GetMapping("user/{id}")
    public void getUser(@PathVAriable(id) Long id){
        //....
    }
}
```

### 四、自定义多参数校验器
- 核心类
> - `CustomValidConstraint`
> - `ValidHandler`

- 使用方式
```java


```