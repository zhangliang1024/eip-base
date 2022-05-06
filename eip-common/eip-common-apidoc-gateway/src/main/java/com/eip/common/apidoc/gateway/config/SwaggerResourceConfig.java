package com.eip.common.apidoc.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.*;

@Slf4j
@Component
@Primary
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    //private final RouteLocator routeLocator;
    //private final GatewayProperties gatewayProperties;

    //@Override
    //public List<SwaggerResource> get() {
    //    List<SwaggerResource> resources = new ArrayList<>();
    //    List<String> routes = new ArrayList<>();
    //    routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
    //    gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).forEach(route -> {
    //        route.getPredicates().stream()
    //                .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
    //                .forEach(predicateDefinition -> resources.add(swaggerResource(route.getId(),
    //                        predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
    //                                .replace("**", "v3/api-docs"))));
    //    });
    //
    //    return resources;
    //}

    /**
     * swagger3默认的url后缀
     */
    private static final String SWAGGER2URL = "/v3/api-docs";

    private static final String APP_CONTEXT_PATH = "context-path";
    /**
     * 网关路由
     */
    private final RouteLocator routeLocator;
    private final SwaggerProperties swaggerProperties;


    @Autowired
    public SwaggerResourceConfig(RouteLocator routeLocator, SwaggerProperties swaggerProperties) {
        this.routeLocator = routeLocator;
        this.swaggerProperties = swaggerProperties;
    }

    public boolean ignoreServer(Route route) {
        String host = route.getUri().getHost();
        List<String> ingoreServerList = swaggerProperties.getIngoreServerList();
        if (StringUtils.isNotBlank(host) && !ingoreServerList.contains(host.toLowerCase())) {
            return true;
        }
        return false;
    }


    /**
     * 对于gateway来说这块比较重要 让swagger能找到对应的服务
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> hosts = new ArrayList<>();
        // 记录已经添加过的server
        Set<String> swaggerUrls = new HashSet<>();

        // 获取所有可用的 host->serviceId
        routeLocator.getRoutes().filter(
                //route -> StringUtils.isNotBlank(route.getUri().getHost()) && !StringUtils.equalsIgnoreCase(applicationName, route.getUri().getHost()))
                route -> ignoreServer(route) )
                .subscribe(route -> {
                    String instance = route.getUri().getHost();
                    Object contextPath = route.getMetadata().get(APP_CONTEXT_PATH);
                    if (Objects.nonNull(contextPath)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(instance).append(String.valueOf(contextPath));
                        hosts.add(sb.toString());
                        //sb.append("/").append(instance.toLowerCase()).append(String.valueOf(contextPath)).append(SWAGGER2URL);
                    } else {
                        hosts.add(instance);
                        //sb.append("/").append(instance.toLowerCase()).append(SWAGGER2URL);
                    }
                    // 拼接url
                    //String url = "/" + instance.toLowerCase() + SWAGGER2URL;
                    //if (!swaggerUrls.contains(url)) {
                    //    swaggerUrls.add(url);
                    //    SwaggerResource swaggerResource = new SwaggerResource();
                    //    swaggerResource.setUrl(url);
                    //    swaggerResource.setName(instance);
                    //    resources.add(swaggerResource);
                    //}
                });



        for (String instance : hosts) {
            // 拼接url
            String url = "/" + instance.toLowerCase() + SWAGGER2URL;
            if (!swaggerUrls.contains(url)) {
                swaggerUrls.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                //if(instance.contains("/")){
                //    String substring = instance.substring(0,instance.indexOf("/"));
                //    swaggerResource.setName(substring);
                //}else {
                    swaggerResource.setName(instance);
                //}
                resources.add(swaggerResource);
            }

        }
        return resources;
    }

}