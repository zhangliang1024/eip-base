package com.eip.common.apidoc.gateway.gateway;

import com.eip.common.apidoc.gateway.config.SwaggerGatewayProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.util.CollectionUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Function: SwaggerProvider
 * Date: 2022年05月16 20:02:04
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
//@Profile({"dev","test"})
public class GatewaySwaggerProvider implements SwaggerResourcesProvider {

    /**
     * 网关路由
     */
    private RouteLocator routeLocator;
    private GatewayProperties gatewayProperties;
    private SwaggerGatewayProperties swaggerGatewayProperties;

    public GatewaySwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties, SwaggerGatewayProperties swaggerGatewayProperties) {
        this.routeLocator = routeLocator;
        this.swaggerGatewayProperties = swaggerGatewayProperties;
        this.gatewayProperties = gatewayProperties;
    }

    /**
     * 聚合其他服务接口
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //取出gateway的route
        routeLocator.getRoutes().filter(
                route -> ignoreServer(route))
                .subscribe(route -> routes.add(route.getId()));
        //结合配置的route-路径(Path)，和route过滤，只获取有效的route节点
        gatewayProperties.getRoutes().stream()
                //过滤路由
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                //循环添加，从路由的断言中获取，一般来说路由都会配置断言Path信息，这就不多说了
                .forEach(route -> {
                    String contextPath = contextPath(route);
                    route.getPredicates().stream()
                            //获取Path信息
                            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                            //开始添加SwaggerResource
                            .forEach(predicateDefinition -> resources.add(
                                    swaggerResource(route.getId(), contextPath,
                                            predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", swaggerGatewayProperties.getApiDocs()))));


                });
        return resources;
    }

    private String contextPath(RouteDefinition route) {
        Object contextPath = route.getMetadata().get(swaggerGatewayProperties.getContextPath());
        if (Objects.nonNull(contextPath)) {
            return contextPath.toString();
        } else {
            return Strings.EMPTY;
        }
    }

    private boolean ignoreServer(Route route) {
        String host = route.getUri().getHost();
        List<String> ingoreServerList = swaggerGatewayProperties.getIngoreServerList();
        if (StringUtils.isNotBlank(host) &&
                !CollectionUtils.isEmpty(ingoreServerList) &&
                !ingoreServerList.contains(host.toLowerCase())) {
            return true;
        }
        return false;
    }

    private SwaggerResource swaggerResource(String name, String contextPath, String url) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        if (StringUtils.isNotBlank(contextPath)) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(contextPath)).append(url);
            swaggerResource.setLocation(sb.toString());
        } else {
            swaggerResource.setLocation(url);
        }
        swaggerResource.setSwaggerVersion(swaggerGatewayProperties.getSwaggerVersion());
        return swaggerResource;
    }
}
