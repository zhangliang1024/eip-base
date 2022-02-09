package com.eip.ability.job.core.route.strategy;


import com.eip.ability.job.core.route.ExecutorRouter;
import com.eip.common.job.core.biz.model.ReturnT;
import com.eip.common.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList){
        return new ReturnT<String>(addressList.get(0));
    }

}
