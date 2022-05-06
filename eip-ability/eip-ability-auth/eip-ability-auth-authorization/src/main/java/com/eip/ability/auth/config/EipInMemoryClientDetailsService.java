package com.eip.ability.auth.config;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: EipInMemoryClientDetailsService
 * Function:
 * Date: 2022年02月21 13:14:03
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class EipInMemoryClientDetailsService implements ClientDetailsService {
    private Map<String, ClientDetails> clientDetailsStore = new HashMap();

    public EipInMemoryClientDetailsService() {
    }

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = (ClientDetails)this.clientDetailsStore.get(clientId);
        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        } else {
            return details;
        }
    }

    public void setClientDetailsStore(Map<String, ? extends ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = new HashMap(clientDetailsStore);
    }


}
