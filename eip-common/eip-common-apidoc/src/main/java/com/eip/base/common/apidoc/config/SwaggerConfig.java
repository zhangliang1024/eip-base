package com.eip.base.common.apidoc.config;

import com.eip.base.common.apidoc.prop.ApidocInfo;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

/**
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2021/11/23 11:22
 * @version：V1.0
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@EnableConfigurationProperties(ApidocInfo.class)
public class SwaggerConfig {

    @Autowired
    ApidocInfo apidocInfo;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30) //文档版本
                .enable(apidocInfo.isEnabled()) //是否开启swagger
                .groupName(apidocInfo.getGroupName())
                .apiInfo(apiInfo()) //
                .select() //选择哪些接口作为swaager的doc发布
                    .apis(RequestHandlerSelectors.basePackage(apidocInfo.getBasePackage()))//扫描包
                    .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//扫描在API注解的contorller
                    .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))//扫描带ApiOperation注解的方法
                .paths(PathSelectors.any()) // 过滤的接口
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes())
                .protocols(new HashSet<>(Arrays.asList("http","https"))) //支持通信协议
                .host(apidocInfo.getHost());//自定义host
    }

    /**
     * Api文档的元信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(apidocInfo.getTitle())
                .description(apidocInfo.getDescription())
                .version(apidocInfo.getVersion())
                .license(apidocInfo.getLicense())
                .licenseUrl(apidocInfo.getLicenseUrl())
                .contact(new Contact(apidocInfo.getContact().getName(),apidocInfo.getContact().getUrl(),apidocInfo.getContact().getEmail()))
                .build();
    }


    private List<SecurityScheme > securitySchemes() {
        List<SecurityScheme > apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }
    /**
     * JWT token
     * @return
     */
    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

}

