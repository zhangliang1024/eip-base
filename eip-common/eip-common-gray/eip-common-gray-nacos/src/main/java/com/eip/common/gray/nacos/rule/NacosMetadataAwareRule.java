package com.eip.common.gray.nacos.rule;

import com.eip.common.gray.core.predicate.DiscoveryEnabledPredicate;
import com.eip.common.gray.core.rule.DiscoveryEnabledRule;
import com.eip.common.gray.nacos.predicate.NacosMetadataAwarePredicate;

/**
 * ClassName: MetadataAwareRule
 * Function:
 * Date: 2023年01月04 14:46:05
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class NacosMetadataAwareRule extends DiscoveryEnabledRule {

    public NacosMetadataAwareRule(){
        this(new NacosMetadataAwarePredicate());
    }
    public NacosMetadataAwareRule(DiscoveryEnabledPredicate predicate) {
        super(predicate);
    }
}
