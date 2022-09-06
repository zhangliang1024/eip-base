package com.eip.ability.auth.custom.client;

import com.eip.ability.auth.custom.enums.PasswordEncoderTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/**
 * ClassName: ClientDetailsServiceImpl
 * Function:
 * Date: 2022年09月05 09:38:48
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Override
    public ClientDetails loadClientByClientId(String clientId)  {
        // 通过Feign从管理端获取

        BaseClientDetails clientDetails = new BaseClientDetails(
                "ams",
                "",
                "all",
                "password,client_credentials,refresh_token,authorization_code",
                "",
                "http://www.baidu.com"
        );

        // 加密方式
        clientDetails.setClientSecret(PasswordEncoderTypeEnum.NOOP.getPrefix() + "ams");
        // 过期时间
        clientDetails.setAccessTokenValiditySeconds(3600);
        clientDetails.setRefreshTokenValiditySeconds(3600000);
        return clientDetails;
    }
}
