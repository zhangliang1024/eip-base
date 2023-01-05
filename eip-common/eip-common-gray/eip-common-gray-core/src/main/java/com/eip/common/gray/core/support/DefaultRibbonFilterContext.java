package com.eip.common.gray.core.support;

import com.eip.common.gray.core.api.RibbonFilterContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DefaultGrayFilterContext
 * Function:
 * Date: 2023年01月04 13:29:11
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class DefaultRibbonFilterContext implements RibbonFilterContext {

    public static final Map<String,String> cache = new HashMap<>();

    @Override
    public RibbonFilterContext add(String key, String value) {
        this.cache.put(key, value);
        return this;
    }

    @Override
    public String get(String key) {
        return this.cache.get(key);
    }

    @Override
    public RibbonFilterContext remove(String key) {
        this.cache.remove(key);
        return this;
    }

    @Override
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.cache);
    }
}
