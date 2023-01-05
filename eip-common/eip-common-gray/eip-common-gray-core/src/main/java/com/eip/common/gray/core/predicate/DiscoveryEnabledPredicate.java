package com.eip.common.gray.core.predicate;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * ClassName: DiscoveryEnabledPredicate
 * Function:
 * Date: 2023年01月04 14:28:39
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public abstract class DiscoveryEnabledPredicate extends AbstractServerPredicate {

    @Override
    public boolean apply(@Nullable PredicateKey predicateKey) {
        return Objects.nonNull(predicateKey) && apply(predicateKey.getServer());
    }

    protected abstract boolean apply(Server server);
}
