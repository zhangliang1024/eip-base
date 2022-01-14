### 一、本地配置覆盖远程，支持灰度
> - `apollo-client`包中`PropertySourcesProcessor`的`initializePropertySources`方法

```java
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

  //...
  private void initializePropertySources() {
    if (environment.getPropertySources().contains(PropertySourcesConstants.APOLLO_PROPERTY_SOURCE_NAME)) {
      //already initialized
      return;
    }
    CompositePropertySource composite;
    if (configUtil.isPropertyNamesCacheEnabled()) {
      composite = new CachedCompositePropertySource(PropertySourcesConstants.APOLLO_PROPERTY_SOURCE_NAME);
    } else {
      composite = new CompositePropertySource(PropertySourcesConstants.APOLLO_PROPERTY_SOURCE_NAME);
    }

    //sort by order asc
    ImmutableSortedSet<Integer> orders = ImmutableSortedSet.copyOf(NAMESPACE_NAMES.keySet());
    Iterator<Integer> iterator = orders.iterator();

    while (iterator.hasNext()) {
      int order = iterator.next();
      for (String namespace : NAMESPACE_NAMES.get(order)) {
        Config config = ConfigService.getConfig(namespace);

        composite.addPropertySource(configPropertySourceFactory.getConfigPropertySource(namespace, config));
      }
    }

    // clean up
    NAMESPACE_NAMES.clear();

    //支持本地配置覆盖远程
    String allowOverride = environment.getProperty("apollo.client.allow-override");
    logger.info("[apollo-client-allow-override] - {}", allowOverride);
    if(Boolean.TRUE.toString().equals(allowOverride)){
        MutablePropertySources propertySources = environment.getPropertySources();
        if(propertySources.contains(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME)){
            PropertySource<?> bootStrapPropertySource = propertySources.get(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME);
            if(bootStrapPropertySource != null && propertySources.precedenceOf(bootStrapPropertySource) != 0){
                propertySources.remove(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME);
                propertySources.addLast(bootStrapPropertySource);
            }
        }
        propertySources.addAfter(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME,composite);
    }else {
        // add after the bootstrap property source or to the first
        if (environment.getPropertySources().contains(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
            // ensure ApolloBootstrapPropertySources is still the first
            ensureBootstrapPropertyPrecedence(environment);
            environment.getPropertySources().addAfter(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME, composite);
        } else {
            environment.getPropertySources().addFirst(composite);
        }
    }
  }
  //...
}

```


### 二、动态刷新时支持自定义解密逻辑
> 目前测试基于`jasypt-spring-boot-starter`-`3.0.3`版本进行配置加密后，会导致动态刷新失效。
> - 解决方案：为降低版本到 - `2.0.0`

- 基于默认加解密逻辑，可以直接根据以下配置来实现配置的加解密
> 指定加解密用到的`password` 即可。`password`可通过环境变量方式来指定，避免泄露
```yaml
jasypt:
  encryptor:
    # 指定加密密钥，生产环境请放到启动参数里面
    password: ${JASYPT_PASSWORD:yinjihaunkey}
    # 指定解密算法，需要和加密时使用的算法一致
    algorithm: PBEWithMD5AndDES
    # 指定initialization vector类型
    iv-generator-classname: org.jasypt.iv.NoIvGenerator
```

- 基于`2.0.0`版本实现自定义加解密实现
> 若要实现自定义的加解密逻辑，则需要覆写`apollo`提供的`DefaultConfig`的`updateConfig`方法。如下：
> - 注意包名：`com.ctrip.framework.apollo.internals`
```java
public class DefaultConfig extends AbstractConfig implements RepositoryChangeListener {

    private void updateConfig(Properties newConfigProperties, ConfigSourceType sourceType) {
        Set<Object> keys = newConfigProperties.keySet();
        for (Object k : keys) {
            String key = k.toString();
            String value = newConfigProperties.getProperty(key);
            // 加密Value
            if (value.startsWith("ENC(") && value.endsWith(")")) {
                logger.debug("[Apollo-dencypt] - value {}", value);
                String privateKey = newConfigProperties.getProperty(APOLLO_ENCRYPT_PRIVATE_KEY);
                // 解密然后重新赋值
                String decrypt = ApolloDencryptService.dencypt(value.substring(4, value.length() - 1), privateKey);
                newConfigProperties.setProperty(key, decrypt);
            }
        }
        m_configProperties.set(newConfigProperties);
        m_sourceType = sourceType;
    }
}
```
![](https://ae04.alicdn.com/kf/H3ea1acfdfcf84ec0907525ec64aab2c2Y.png)


### 三、扩展自定义监听命名空间
> - `ApolloListenerNamespac`
```java
public interface ApolloListenerNamespace {

    default String[] value() {
        String[] defaultNamespaces = {ConfigConsts.NAMESPACE_APPLICATION};
        String[] nameSpaces = listenerNameSpaces();
        if(Objects.isNull(nameSpaces)){
            return defaultNamespaces;
        }
        return nameSpaces;
    }
    /**
     * 监听命名空间
     */
    String[] listenerNameSpaces();

    /**
     * 监听配置Property类
     */
    List<String> bindNames();
}
```