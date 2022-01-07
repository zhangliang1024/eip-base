

```text

调用RestTemplate的默认构造函数，RestTemplate对象在底层通过使用java.net包下的实现创建HTTP 请求，可以通过使用ClientHttpRequestFactory指定不同的HTTP请求方式。

ClientHttpRequestFactory接口主要提供了两种实现方式
一种是SimpleClientHttpRequestFactory，使用J2SE提供的方式（既java.net包提供的方式）创建底层的Http请求连接。
一种方式是使用HttpComponentsClientHttpRequestFactory方式，底层使用HttpClient访问远程的Http服务，使用HttpClient可以配置连接池和证书等信息。

RestTemplate默认是使用SimpleClientHttpRequestFactory，内部是调用jdk的HttpConnection，默认超时为-1
```


### 三、参考文档
[RestTemplate的设置和使用](https://www.cnblogs.com/chenjfblog/p/8253100.html)
[RestTemplate使用教程](https://www.jianshu.com/p/35aca2e31f06)
[]()
[]()