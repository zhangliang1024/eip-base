package com.eip.common.gray.core.support;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.eip.common.gray.core.api.RibbonFilterContext;

/**
 * ClassName: GrayFilterContextHolder
 * Function:
 * Date: 2023年01月04 13:32:03
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class RibbonFilterContextHolder {

    private static final TransmittableThreadLocal<RibbonFilterContext> contextHolder = new TransmittableThreadLocal<RibbonFilterContext>() {
        @Override
        protected RibbonFilterContext initialValue() {
            return new DefaultRibbonFilterContext();
        }
    };


    public static RibbonFilterContext getCurrentContext() {
        return (RibbonFilterContext) contextHolder.get();
    }

    public static void clearCurrentContext() {
        contextHolder.remove();
    }
}
