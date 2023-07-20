## `Ribbon超时配置核心类`


```java
public class RibbonClientConfiguration {

    /**
     * ribbon 默认的超时时间，
     * 在不设置Feign的超时情况下 这里会覆盖调feign的默认超时
     */
    @Bean
    @ConditionalOnMissingBean
    public IClientConfig ribbonClientConfig() {
        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
        config.loadProperties(this.name);
        config.set(CommonClientConfigKey.ConnectTimeout, 1000);
        config.set(CommonClientConfigKey.ReadTimeout, 1000);
        config.set(CommonClientConfigKey.GZipPayload, true);
        return config;
    }
}
```
> `Ribbon参数配置类`
```text
RibbonProperties
CommonClientConfigKey

DefaultClientConfigImpl

```

```java

public class LoadBalancerFeignClient implements Client {

    /**
     * Feign loadbalancer执行逻辑
     */
    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        try {
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            URI uriWithoutHost = cleanUrl(request.url(), clientName);
            FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                    this.delegate, request, uriWithoutHost);

            IClientConfig requestConfig = getClientConfig(options, clientName);
            return lbClient(clientName)
                    .executeWithLoadBalancer(ribbonRequest, requestConfig).toResponse();
        }
        catch (ClientException e) {
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        }
    }
}

public class FeignLoadBalancer {

    public FeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig,
                             ServerIntrospector serverIntrospector) {
        super(lb, clientConfig);
        this.setRetryHandler(RetryHandler.DEFAULT);
        this.clientConfig = clientConfig;
        this.ribbon = RibbonProperties.from(clientConfig);
        // Ribbon 自定义超时间 在这里初始化
        RibbonProperties ribbon = this.ribbon;
        this.connectTimeout = ribbon.getConnectTimeout();
        this.readTimeout = ribbon.getReadTimeout();
        this.serverIntrospector = serverIntrospector;
    }
    
    /**
     * Feign的请求调用会走到这里
     *
     * configOverride为封装好的，请求调用参数。在这里重新new Request.Options() 组装Feign调用参数
     */
    @Override
    public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride)
            throws IOException {
        Request.Options options;
        if (configOverride != null) {
            RibbonProperties override = RibbonProperties.from(configOverride);
            // Ribbon 自定义超时间，在这里通过 this.connectTimeout 做为默认值传入
            options = new Request.Options(override.connectTimeout(this.connectTimeout),
                    override.readTimeout(this.readTimeout));
        } else {
            options = new Request.Options(this.connectTimeout, this.readTimeout);
        }
        Response response = request.client().execute(request.toRequest(), options);
        return new RibbonResponse(request.getUri(), response);
    }
}
```

> `Feign` 配置类
```text
FeignClientProperties


```

```java
/**
 * LoabBalancerFeignClient init
 */
@Configuration(proxyBeanMethods = false)
class DefaultFeignLoadBalancedConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) {
        return new LoadBalancerFeignClient(new Client.Default(null, null), cachingFactory,
                clientFactory);
    }

}

/**
 * Feign 执行请求
 */
public final class Request {
    
}

public static class Default implements Client {

    public Response execute(Request request, Request.Options options) throws IOException {
        HttpURLConnection connection = this.convertAndSend(request, options);
        return this.convertResponse(connection, request);
    }

    /**
     * Feign 底层执行请求调用
     */
    HttpURLConnection convertAndSend(Request request, Request.Options options) throws IOException {
        URL url = new URL(request.url());
        HttpURLConnection connection = this.getConnection(url);
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection sslCon = (HttpsURLConnection) connection;
            if (this.sslContextFactory != null) {
                sslCon.setSSLSocketFactory(this.sslContextFactory);
            }

            if (this.hostnameVerifier != null) {
                sslCon.setHostnameVerifier(this.hostnameVerifier);
            }
        }

        connection.setConnectTimeout(options.connectTimeoutMillis());
        connection.setReadTimeout(options.readTimeoutMillis());
        connection.setAllowUserInteraction(false);
        connection.setInstanceFollowRedirects(options.isFollowRedirects());
        connection.setRequestMethod(request.httpMethod().name());
        Collection<String> contentEncodingValues = (Collection) request.headers().get("Content-Encoding");
        boolean gzipEncodedRequest = contentEncodingValues != null && contentEncodingValues.contains("gzip");
        boolean deflateEncodedRequest = contentEncodingValues != null && contentEncodingValues.contains("deflate");
        boolean hasAcceptHeader = false;
        Integer contentLength = null;
        Iterator var10 = request.headers().keySet().iterator();
        // ...
    }
}
```

```java
/**
 * Zuul 网关底层走RibbonLoadbalacer 执行负载路由
 */
public class RibbonLoadBalancingHttpClient{
    public RibbonApacheHttpResponse execute(RibbonApacheHttpRequest request, final IClientConfig configOverride) throws Exception {
        IClientConfig config = configOverride != null ? configOverride : this.config;
        RibbonProperties ribbon = RibbonProperties.from(config);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(ribbon.connectTimeout(this.connectTimeout)).setSocketTimeout(ribbon.readTimeout(this.readTimeout)).setRedirectsEnabled(ribbon.isFollowRedirects(this.followRedirects)).setContentCompressionEnabled(ribbon.isGZipPayload(this.gzipPayload)).build();
        request = this.getSecureRequest(request, configOverride);
        HttpUriRequest httpUriRequest = request.toRequest(requestConfig);
        HttpResponse httpResponse = ((CloseableHttpClient)this.delegate).execute(httpUriRequest);
        return new RibbonApacheHttpResponse(httpResponse, httpUriRequest.getURI());
    }  
}

```


```java
class FeignClientFactoryBean{

    /**
     * Feign 构建过程
     */
    protected Feign.Builder feign(FeignContext context) {
        FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
        Logger logger = loggerFactory.create(type);

        // @formatter:off
        Feign.Builder builder = get(context, Feign.Builder.class)
                // required values
                .logger(logger)
                .encoder(get(context, Encoder.class))
                .decoder(get(context, Decoder.class))
                .contract(get(context, Contract.class));
        // @formatter:on

        configureFeign(context, builder);

        return builder;
    }
}

```


```java


```

```java


```

