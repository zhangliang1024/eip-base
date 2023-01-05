package com.eip.common.gray.eureka.rule;

import com.eip.common.gray.core.predicate.DiscoveryEnabledPredicate;
import com.eip.common.gray.core.rule.DiscoveryEnabledRule;
import com.eip.common.gray.eureka.predicate.EurekaMetadataAwarePredicate;

/**
 * ClassName: MetadataAwareRule
 * Function:
 * Date: 2023年01月04 14:46:05
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class EurekaMetadataAwareRule extends DiscoveryEnabledRule {

    public EurekaMetadataAwareRule(){
        this(new EurekaMetadataAwarePredicate());
    }
    public EurekaMetadataAwareRule(DiscoveryEnabledPredicate predicate) {
        super(predicate);
    }
}
