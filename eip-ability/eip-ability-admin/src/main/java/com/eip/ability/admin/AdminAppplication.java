package com.eip.ability.admin;

import com.eip.ability.admin.log.event.SysLogListener;
import com.eip.ability.admin.service.OptLogService;
import com.eip.ability.admin.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;

/**
 * ClassName: AdminAppplication
 * Function: 管理系统
 * Date: 2022年09月19 13:14:46
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.eip.**.mapper", annotationClass = Repository.class)
public class AdminAppplication {

    @SneakyThrows
    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(AdminAppplication.class, args);
        Environment env = applicationContext.getEnvironment();
        final String appName = env.getProperty("spring.application.name");
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = StringUtils.defaultString(env.getProperty("server.port"), "8080");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Doc: \thttp://{}:{}/swagger-ui.html\n" +
                        "----------------------------------------------------------",
                appName, host, port);
    }

    @Bean
    public SysLogListener sysLogListener(OptLogService optLogService) {
        return new SysLogListener(optLogService::save);
    }

}
