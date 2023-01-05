package com.eip.common.gray.eureka.predicate;

import com.eip.common.gray.core.api.RibbonFilterContext;
import com.eip.common.gray.core.predicate.DiscoveryEnabledPredicate;
import com.eip.common.gray.core.support.RibbonFilterContextHolder;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

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
public class EurekaMetadataAwarePredicate extends DiscoveryEnabledPredicate {

    @Override
    protected boolean apply(Server server) {
        if(server instanceof DiscoveryEnabledServer){
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer)server;
            final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
            final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
            final Map<String, String> metadata = discoveryEnabledServer.getInstanceInfo().getMetadata();
            return metadata.entrySet().containsAll(attributes);
        }
        return false;
    }
}
