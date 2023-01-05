package com.eip.common.gray.core.api;

import java.util.Map;

/**
 * ClassName: GrayFilterContext
 * Function:
 * Date: 2023年01月04 13:27:08
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface RibbonFilterContext {

    RibbonFilterContext add(String key, String value);

    String get(String key);

    RibbonFilterContext remove(String key);

    Map<String, String> getAttributes();
}
