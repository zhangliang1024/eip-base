package com.eip.common.apidoc.lisenter;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Slf4j
@AllArgsConstructor
public class ApplicationAccessUrlRunner implements ApplicationRunner {

    private static final String DEFAULT_NAME = "default";
    private static final String DEFAULT_PORT = "8080";

    private final Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String applicationName = StrUtil.isBlank(env.getProperty("spring.application.name")) ? DEFAULT_NAME : env.getProperty("spring.application.name");
        String port = StrUtil.isBlank(env.getProperty("server.port")) ? DEFAULT_PORT : env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application [{}] is running! Access URLs:\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Swagger: \thttp://{}:{}/swagger-ui/index.html\n\t" +
                        "Doc: \t\thttp://{}:{}/doc.html\n" +
                        "----------------------------------------------------------",
                applicationName,
                InetAddress.getLocalHost().getHostAddress(), port,
                InetAddress.getLocalHost().getHostAddress(), port,
                InetAddress.getLocalHost().getHostAddress(), port
        );
    }
}