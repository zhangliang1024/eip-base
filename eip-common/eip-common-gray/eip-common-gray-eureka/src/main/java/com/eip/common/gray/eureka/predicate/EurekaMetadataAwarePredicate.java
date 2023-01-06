package com.eip.common.gray.eureka.predicate;

import com.eip.common.gray.core.api.RibbonFilterContext;
import com.eip.common.gray.core.handler.GrayServiceHandler;
import com.eip.common.gray.core.predicate.DiscoveryEnabledPredicate;
import com.eip.common.gray.core.support.RibbonFilterContextHolder;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: MetadataAwarePredicate
 * Function:
 * Date: 2023年01月04 14:43:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class EurekaMetadataAwarePredicate extends DiscoveryEnabledPredicate {

    @Override
    protected boolean apply(Server server) {
        log.info("[GRAY-RIBBON] ============= CHOOSE SERVER BEGIN =============");
        if (server instanceof DiscoveryEnabledServer) {
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
            String appName = discoveryEnabledServer.getInstanceInfo().getAppName().toLowerCase();
            String host = discoveryEnabledServer.getHost();
            int port = discoveryEnabledServer.getPort();
            final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
            final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
            final Map<String, String> metadata = discoveryEnabledServer.getInstanceInfo().getMetadata();
            log.info("[GRAY-RIBBON] Discovery server: [{}:{}:{}] metadata: [{}]", appName, host, port, metadata);
            log.info("[GRAY-RIBBON] Ribbon grayVersion: [{}]", context.get(GrayServiceHandler.GRAY_VERSION));
            if (metadata.entrySet().containsAll(attributes)) {
                log.info("[GRAY-RIBBON] Ribbon choose server: [{}-{}:{}] grayVersion: [{}]", appName, host, port, context.get(GrayServiceHandler.GRAY_VERSION));
                log.info("[GRAY-RIBBON] ============= CHOOSE SERVER END =============\n");
                return true;
            }
        }
        log.info("[GRAY-RIBBON] ============= CHOOSE SERVER END =============\n");
        return false;
    }
}
