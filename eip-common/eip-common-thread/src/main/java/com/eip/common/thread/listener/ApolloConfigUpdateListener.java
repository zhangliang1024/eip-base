package com.eip.common.thread.listener;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.eip.common.thread.config.DynamicThreadPoolProperties;
import com.eip.common.thread.core.DynamicThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @Author: Barnett
 * @Date: 2021/11/8 17:53
 * @Description: Apollo配置修改监听
 */
@Slf4j
public class ApolloConfigUpdateListener {

    @Value("${apollo.bootstrap.namespaces:application}")
    private String namespace;

    @Autowired
    private DynamicThreadPoolManager dynamicThreadPoolManager;

    @Autowired
    private DynamicThreadPoolProperties poolProperties;

    @PostConstruct
    public void init() {
        initConfigUpdateListener();
    }

    public void initConfigUpdateListener() {
        String apolloNamespace = namespace;
        if (StringUtils.hasText(poolProperties.getApolloNamespace())) {
            apolloNamespace = poolProperties.getApolloNamespace();
        }

        String finalApolloNamespace = apolloNamespace;
        Config config = ConfigService.getConfig(finalApolloNamespace);

        config.addChangeListener(changeEvent -> {

            //等待Apollo配置刷新完成
            waitConfigRefreshOver();

            ConfigFileFormat configFileFormat = ConfigFileFormat.Properties;
            String getConfigNamespace = finalApolloNamespace;
            if (finalApolloNamespace.contains(ConfigFileFormat.YAML.getValue())) {
                configFileFormat = ConfigFileFormat.YAML;
                // 去除.yaml后缀，getConfigFile时候会根据类型自动追加
                getConfigNamespace = getConfigNamespace.replaceAll("." + ConfigFileFormat.YAML.getValue(), "");
            }

            ConfigFile configFile = ConfigService.getConfigFile(getConfigNamespace, configFileFormat);
            String content = configFile.getContent();

            if (finalApolloNamespace.contains(ConfigFileFormat.YAML.getValue())) {
                poolProperties.refreshYaml(content);
            } else {
                poolProperties.refresh(content);
            }

            dynamicThreadPoolManager.refreshThreadPoolExecutor();
            log.info("[dynamic pool] - apollo config refresh finished");
        });
        log.info("[dynamic pool] - apollo config listener init");
    }

    private void waitConfigRefreshOver() {
        if (poolProperties.isBoolWaitRefreshConfig()) {
            try {
                Thread.sleep(poolProperties.getWaitRefreshConfigSeconds() * 1000);
            } catch (InterruptedException e) {}
        }
    }

}
