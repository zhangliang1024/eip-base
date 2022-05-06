package com.eip.ability.auth.config;

import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: EipInMemoryClientDetailsServiceBuilder
 * Function:
 * Date: 2022年02月21 13:11:49
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class EipInMemoryClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<EipInMemoryClientDetailsServiceBuilder> {

    private Map<String, ClientDetails> clientDetails = new HashMap();

    public EipInMemoryClientDetailsServiceBuilder() {
    }

    protected void addClient(String clientId, ClientDetails value) {
        this.clientDetails.put(clientId, value);
    }

    protected ClientDetailsService performBuild() {
        EipInMemoryClientDetailsService clientDetailsService = new EipInMemoryClientDetailsService();
        clientDetailsService.setClientDetailsStore(this.clientDetails);
        return clientDetailsService;
    }


}
