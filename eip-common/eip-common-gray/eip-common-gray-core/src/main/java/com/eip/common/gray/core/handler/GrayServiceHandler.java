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

    public static final String GRAY_VERSION = "grayVersion";

    protected boolean handle(String serviceId) {
        String grayVersion = getGrayVersion(serviceId);
        if (StringUtils.isNotBlank(grayVersion)) {
            RibbonFilterContextHolder.clearCurrentContext();
            RibbonFilterContextHolder.getCurrentContext().add(GRAY_VERSION, grayVersion);
            return true;
        }
        return false;
    }

    /**
     * 支持通过子类实现来扩展自定义访问策略
     */
    protected abstract String getGrayVersion(String serviceId);
}
