# Web组件


### 三、接口多版本
> 最好的版本控制方法是不进行版本控制：构建向后兼容的服务，以便尽可能避免版本控制！
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




### 七、参考文档
* [一口气说出 4 种 API 版本控制方案](https://mp.weixin.qq.com/s/DCEsbzOM1ZJajjHj5WxiIg)