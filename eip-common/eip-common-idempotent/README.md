

### 概念
> 幂等：任意多次执行所产生的影响均与一次执行的影响相同。(最终的好含义：对数据库的影响只能是一次性的，不能重复处理)


#### 什么情况下需要做幂等处理
```properties
1. 前端多次提交。比如某个业务处理需要2秒钟，在2秒钟内，点击了多次请求。非幂等的接口会进行多次处理
2. 响应超时导致请求重复。在微服务调用过程中，很多组件有重试功能，可能导致重复请求
```

### 保证幂等的手段
> 1.数据库建立唯一索引,可以保证插入数据库的记录只有一条                                                 
> 2.token机制,每次接口请求先获取一个token.然后在请求时携带token后台进行校验，通过则删除
> 3.悲观锁或乐观锁，悲观锁保证每次for update时其它SQL无法更新数据
> 4.先查询后判断,查询数据是否存在或处理过，若存在证明已处理，直接拒绝。若没有则证明第一次处理，直接放行

#### 作用
```properties
1. 防止产生脏数据或乱数据
2. 也可以限制并发
```


### 幂等实现原理
![](https://img-blog.csdnimg.cn/img_convert/8cad7fd189bd0bc155445d442ef8c638.webp?x-oss-process=image/format,png)



### 参考文档
[redis-auto-idempotent-spring-boot-starter一个高并发下接口幂等性处理的自定义starter](https://www.yht7.com/news/85813)
[瞬间几千次的重复提交，我用Spring Boot+Redis扛住了](https://blog.csdn.net/leeta521/article/details/119109224)
[如何设计一个幂等接口](https://cloud.tencent.com/developer/article/1637397)
[]()
[]()
[]()



### 开源代码
[redis实现幂等性使用只需一个注解的-idempotent](https://github.com/0yuyuko0/idempotent)