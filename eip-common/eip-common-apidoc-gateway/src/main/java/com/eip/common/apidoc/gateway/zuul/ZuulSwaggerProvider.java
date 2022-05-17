package com.eip.common.apidoc.gateway.zuul;

import com.eip.common.apidoc.gateway.config.SwaggerGatewayProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Function: ZuulSwaggerProvider
 * Date: 2022年05月16 20:02:04
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class ZuulSwaggerProvider implements SwaggerResourcesProvider {

    private RouteLocator routeLocator;
    private ZuulProperties zuulProperties;
    private SwaggerGatewayProperties swaggerGatewayProperties;

    public ZuulSwaggerProvider(RouteLocator routeLocator,
                               ZuulProperties zuulProperties,
                               SwaggerGatewayProperties swaggerGatewayProperties) {
        this.routeLocator = routeLocator;
        this.swaggerGatewayProperties = swaggerGatewayProperties;
        this.zuulProperties = zuulProperties;
    }

    @Override
    public List<SwaggerResource> get() {

        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //取出gateway的route
        routeLocator.getRoutes().forEach(route -> {
            if (ignoreServer(route)) {
                routes.add(route.getId());
            }
        });

        Map<String, ZuulProperties.ZuulRoute> zuulRouteMap = zuulProperties.getRoutes();
        zuulRouteMap.entrySet().stream()
                .filter(zuulRoute -> routes.contains(zuulRoute.getValue().getId()))
                .forEach(zuulRoute -> {
                    resources.add(
                            swaggerResource(zuulRoute.getKey(),
                                    zuulRoute.getValue().getPath().replace("/**", swaggerGatewayProperties.getApiDocs())));
                });
        return resources;
    }

    public boolean ignoreServer(Route route) {
        String host = route.getId();
        List<String> ingoreServerList = swaggerGatewayProperties.getIngoreServerList();
        if (StringUtils.isNotBlank(host) && !ingoreServerList.contains(host.toLowerCase())) {
            return true;
        }
        return false;
    }

    private SwaggerResource swaggerResource(String name, String url) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(url);
        swaggerResource.setSwaggerVersion(swaggerGatewayProperties.getSwaggerVersion());
        return swaggerResource;
    }

}