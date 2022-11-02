package com.eip.ability.gateway;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * ClassName: GatewayApplication
 * Function:
 * Date: 2022年01月07 14:41:18
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {


    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        final String appName = env.getProperty("spring.application.name");
        String port = StringUtils.defaultString(env.getProperty("server.port"), "8080");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Doc: \thttp://{}:{}/swagger-ui.html\n" +
                        "----------------------------------------------------------",
                appName, "localhost", port);
    }
}
