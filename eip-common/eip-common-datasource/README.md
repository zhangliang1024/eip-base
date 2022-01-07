# dynamic-datasource-spring-boot-starter
> springboot starter 通过ThreadLocalt+AOP 方式实现读写分离、多数据源动态切换。
>
> 通过添加 `@Master`注解，支持特殊场景下必须使用主数据源的业务。
>
> 需要提前准备好，主从数据库环境。基于1主2从架构，暂不支持扩展。
>

[演示demo](https://github.com/zhangliang1024/spring-fox-spring-boot-sample)

### 一、介绍
> AOP
```java
@Aspect
@Component
public class DataSourceAop {

    /**
     * 只读：
     * 不是Master注解的对象或方法  && select开头的方法  ||  get开头的方法
     */
    @Pointcut("!@annotation(com.zhliang.pzy.dynamic.datasource.annotation.Master)"+
            "&&(execution(* com..*Impl.select*(..))"+
            "|| execution(* com..*Impl.get*(..))" +
            "|| execution(* com..*Impl.find*(..)))")
    public void readPointcut(){
    }

    /**
     * 写：
     * Master注解的对象或方法 || insert开头的方法  ||  add开头的方法 || update开头的方法
     * || edlt开头的方法 || delete开头的方法 || remove开头的方法
     */
    @Pointcut("@annotation(com.zhliang.pzy.dynamic.datasource.annotation.Master) " +
            "|| execution(* com..*Impl.insert*(..)) " +
            "|| execution(* com..*Impl.add*(..)) " +
            "|| execution(* com..*Impl.update*(..)) " +
            "|| execution(* com..*Impl.edit*(..)) " +
            "|| execution(* com..*Impl.delete*(..)) " +
            "|| execution(* com..*Impl.remove*(..))"+
            "|| execution(* com..*Impl.save*(..))")
    public void writePointcut(){
    }

    @Before("readPointcut()")
    public void read(){
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write(){
        DBContextHolder.master();
    }
}
```
> ThreadLocal 
```java
public class DBContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DBContextHolder.class);

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void setContextHolder(DBTypeEnum dbType){
        contextHolder.set(dbType);
    }
    public static DBTypeEnum getContextHolder(){
        return contextHolder.get();
    }

    public static void master(){
        setContextHolder(DBTypeEnum.MASTER);
        logger.info("Datasource is useing master ");
    }

    public static void slave(){
        //轮询
        int index = counter.getAndIncrement() % 2;
        if(counter.get() > 9999){
            counter.set(-1);
        }
        if(index == 0){
            setContextHolder(DBTypeEnum.SLAVE01);
            logger.info("Datasource is useing slave01 ");
        }else {
            setContextHolder(DBTypeEnum.SLAVE02);
            logger.info("Datasource is useing slave02 ");
        }
    }

}
```

### 二、使用说明
> pom.xml
```xml
<!-- Mybatis Mysql 依赖-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.0.1</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
</dependency>
<dependency>
    <groupId>com.zhliang.pzy</groupId>
    <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
> application.yaml
```yaml
spring:
  datasource:
    master:
      jdbc-url: jdbc:mysql://192.168.80.134:3306/test?useUnicode=true&characterEncoding=gbk&zeroDateTimeBehavior=convertToNull&useSSL=false
      username: root
      password: 123456
      driver-class-name: com.mysql.jdbc.Driver
      type: com.zaxxer.hikari.HikariDataSource
      connectionTimeout: 30000
      validationTimeout: 5000
      maxPoolSize: 200
      minIdle: 100
    slave01:
      jdbc-url: jdbc:mysql://192.168.80.135:3306/test?useUnicode=true&characterEncoding=gbk&zeroDateTimeBehavior=convertToNull&useSSL=false
      username: root
      password: 123456
      driver-class-name: com.mysql.jdbc.Driver
      type: com.zaxxer.hikari.HikariDataSource
      connectionTimeout: 30000
      validationTimeout: 5000
      maxPoolSize: 200
      minIdle: 100
    slave02:
      jdbc-url: jdbc:mysql://192.168.80.135:3306/test?useUnicode=true&characterEncoding=gbk&zeroDateTimeBehavior=convertToNull&useSSL=false
      username: root
      password: 123456
      driver-class-name: com.mysql.jdbc.Driver
      type: com.zaxxer.hikari.HikariDataSource
      connectionTimeout: 30000
      validationTimeout: 5000
      maxPoolSize: 200
      minIdle: 100
      
#mybatis
mybatis:
  ###把xml文件放在com.XX.mapper.*中可能会出现找到的问题，这里把他放在resource下的mapper中
  mapper-mapperLocations: classpath*:mapper/*.xml
  type-aliases-package: com.zhliang.pzy.spring.fox.dynamic.datasource.po
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    call-setters-on-nulls: true
    useGeneratedKeys: true      
```
> 代码
```java
public interface TestService {

    int save(Test t);

    Test findT(int id);

    void update(Test t);

    List<Test> selectList();
}
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper tMapper;

    @Override
    public int save(Test t) {
        return tMapper.insert(t);
    }

    @Master
    @Override
    public Test findT(int id) {
        return tMapper.selectById(id);
    }

    @Override
    public void update(Test t) {
        tMapper.updateByPrimarkey(t);
    }

    @Override
    public List<Test> selectList() {
        return tMapper.selectAll();
    }
}
```

### 三、参考文档
[微服务-springboot-读写分离（多数据源切换）](https://www.cnblogs.com/qjm201000/p/10362790.html)
[springboot 多数据源 读写分离 AOP方式](https://blog.csdn.net/Angry_Mills/article/details/89417115)