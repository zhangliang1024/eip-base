package com.eip.business.demo.config;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.eip.common.config.apollo.service.ApolloListenerNamespace;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName: MyApolloListenerNamespace
 * Function:
 * Date: 2022年01月11 17:18:49
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service
public class MyApolloListenerNamespace implements ApolloListenerNamespace {

    @Override
    public String[] listenerNameSpaces() {
        String[] defaultNamespaces = {ConfigConsts.NAMESPACE_APPLICATION,"db","redis"};
        return defaultNamespaces;
    }

    @Override
    public List<String> bindNames() {
        return Arrays.asList("");
    }
}
