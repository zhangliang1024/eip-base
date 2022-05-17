package com.eip.common.apidoc.gateway.listener;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Slf4j
@AllArgsConstructor
public class ApplicationAccessUrlRunner implements ApplicationRunner {

    private static final String DEFAULT_NAME = "default";
    private static final String DEFAULT_PORT = "8080";
    private static final String DEFAULT_HOST = "localhost";
    private static final String CONTEXT_PATH = "";

    private final Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String applicationName = StrUtil.isBlank(env.getProperty("spring.application.name")) ? DEFAULT_NAME : env.getProperty("spring.application.name");
        String port = StrUtil.isBlank(env.getProperty("server.port")) ? DEFAULT_PORT : env.getProperty("server.port");
        String contextPath = StrUtil.isBlank(env.getProperty("server.servlet.context-path")) ? CONTEXT_PATH : env.getProperty("server.servlet.context-path");

        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String defaultHost = DEFAULT_HOST;
        if (StringUtils.isNotBlank(contextPath)) {
            hostAddress = hostAddress + contextPath;
            defaultHost = defaultHost + contextPath;
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://{}:{}/doc.html\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \thttp://{}:{}/doc.html\n" +
                        "----------------------------------------------------------",

                applicationName, defaultHost, port,
                hostAddress, port,
                hostAddress, port);
    }
}