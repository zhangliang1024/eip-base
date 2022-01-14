package com.eip.common.config.apollo.service;

import com.ctrip.framework.apollo.core.ConfigConsts;

import java.util.List;
import java.util.Objects;

/**
 * ClassName: ApolloListenerNamespace
 * Function:
 * Date: 2022年01月11 16:19:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface ApolloListenerNamespace {

    default String[] value() {
        String[] defaultNamespaces = {ConfigConsts.NAMESPACE_APPLICATION};
        String[] nameSpaces = listenerNameSpaces();
        if(Objects.isNull(nameSpaces)){
            return defaultNamespaces;
        }
        return nameSpaces;
    }

    /**
     * 监听命名空间
     */
    String[] listenerNameSpaces();

    /**
     * 监听配置Property类
     */
    List<String> bindNames();

}
