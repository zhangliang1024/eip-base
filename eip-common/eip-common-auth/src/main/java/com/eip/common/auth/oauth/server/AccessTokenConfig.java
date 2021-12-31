package com.eip.common.auth.oauth.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * ClassName: AccessTokenConfig
 * Function: 令牌存储策略配置
 * Date: 2021年12月17 18:02:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class AccessTokenConfig {


    /**
     * TODO 张良 为方便测试，使用内存存储策略
     */
    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

}
