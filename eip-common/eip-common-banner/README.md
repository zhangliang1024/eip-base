# Banner组件

### 代码
```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        // 启动颜色格式化
        // 这不是唯一启动颜色格式的方式，有兴趣的同学可以查看源码
        /**
         * 1. AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
         * 2. 在`src/main/resources`目录下新建文件`application.properties`,
         *    内容为：`spring.output.ansi.enabled=always`
         *
         * 重要：如果配置第二种方式，第一种方式就不会起作用
         */
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        new SpringApplicationBuilder(EurekaServerApplication.class)//
                .main(SpringVersion.class) // 这个是为了可以加载 Spring 版本
                .bannerMode(Banner.Mode.CONSOLE)// 控制台打印
                .run(args);

    }
}
```


### 文档
* [自定义banner](https://www.bootschool.net/ascii)
* [Spring Boot自定义Banner](https://www.jianshu.com/p/a53f324c92f2)
* [pom文件resources导致资源加载失败](https://blog.csdn.net/qq_41250229/article/details/115422612)