package com.eip.common.gray.nacos.predicate;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.eip.common.gray.core.api.RibbonFilterContext;
import com.eip.common.gray.core.predicate.DiscoveryEnabledPredicate;
import com.eip.common.gray.core.support.RibbonFilterContextHolder;
import com.netflix.loadbalancer.Server;

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
public class NacosMetadataAwarePredicate extends DiscoveryEnabledPredicate {

    @Override
    protected boolean apply(Server server) {
        if (server instanceof NacosServer) {
            NacosServer nacosServer = (NacosServer) server;
            final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
            final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
            final Map<String, String> metadata = nacosServer.getMetadata();
            return metadata.entrySet().containsAll(attributes);
        }
        return false;
    }
}
