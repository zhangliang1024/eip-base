package com.eip.common.gray.core.rule;

import com.eip.common.gray.core.predicate.DiscoveryEnabledPredicate;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;

/**
 * ClassName: DiscoveryEnabledRule
 * Function:
 * Date: 2023年01月04 14:34:00
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public abstract class DiscoveryEnabledRule extends PredicateBasedRule {

    private final CompositePredicate predicate;

    public DiscoveryEnabledRule(DiscoveryEnabledPredicate predicate){
        this.predicate = createCompositePredicate(predicate,new AvailabilityPredicate(this,null));
    }

    private CompositePredicate createCompositePredicate(DiscoveryEnabledPredicate predicate, AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(predicate,availabilityPredicate).build();
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return this.predicate;
    }
}
