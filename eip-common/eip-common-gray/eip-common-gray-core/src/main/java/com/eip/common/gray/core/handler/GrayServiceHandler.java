package com.eip.common.gray.core.handler;

import com.eip.common.gray.core.support.RibbonFilterContextHolder;
import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: ServiceLancherHandler
 * Function:
 * Date: 2023年01月04 15:07:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public abstract class GrayServiceHandler {

    protected boolean handle(String serviceId) {
        String grayVersion = getGrayVersion(serviceId);
        if (StringUtils.isNotBlank(grayVersion)) {
            RibbonFilterContextHolder.clearCurrentContext();
            RibbonFilterContextHolder.getCurrentContext().add("lancher", grayVersion);
            return true;
        }
        return false;
    }


    protected abstract String getGrayVersion(String serviceId);
}
