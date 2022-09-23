package com.eip.common.apidoc.config;

import com.eip.common.apidoc.prop.ApidocInfo;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

/**
 * ClassName: SpringDocConfig
 * Function:
 * Date: 2022年09月23 14:14:38
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(ApidocInfo.class)
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true)
public class SpringDocConfig {

    private final ApidocInfo apidocInfo;

    @Bean
    public OpenAPI springShopOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .externalDocs(externalDocs())
                .addSecurityItem(securityItem())
                .components(components());
    }

    // 创建元数据
    private Info apiInfo() {
        return new Info()
                .title(apidocInfo.getTitle()) //标题
                .description(apidocInfo.getDescription())   //简短描述
                .version(apidocInfo.getVersion())  //API文档的版本信息
                .license(license()) //许可协议
                .contact(createCon()) //公开的API的联系人信息
                //.termsOfService("http://localhost:8080")  //指向服务条款的URL地址，必须是URL地址格式
                ;
    }

    // 许可协议
    private License license() {
        return new License()
                .name(apidocInfo.getLicense()).url(apidocInfo.getLicenseUrl());
    }

    // 创建公开的API的联系人信息
    private Contact createCon() {
        return new Contact()
                .name(apidocInfo.getContact().getName()).email(apidocInfo.getContact().getEmail()).url(apidocInfo.getContact().getUrl());
    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .description(apidocInfo.getExternalDocs().getDescription()).url(apidocInfo.getExternalDocs().getUrl());
    }

    private SecurityRequirement securityItem() {
        return new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION);
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                        new SecurityScheme()
                                .name(HttpHeaders.AUTHORIZATION)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                );
    }

    @Bean
    public GroupedOpenApi systemApi() {
        String[] pathsToMatch = apidocInfo.getPathsToMatch().toArray(new String[]{});
        return GroupedOpenApi.builder().group(apidocInfo.getGroupName()).pathsToMatch(pathsToMatch).build();
    }

}
