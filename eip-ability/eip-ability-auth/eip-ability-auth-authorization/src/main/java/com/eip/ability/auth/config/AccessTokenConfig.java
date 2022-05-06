package com.eip.ability.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * ClassName: AccessTokenConfig
 * Function: Token令牌存储策略
 * 支持多种方式存储：如 内存、Redis、JWT
 * Date: 2022年01月17 16:42:11
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Configuration
public class AccessTokenConfig {

    /**
     * 令牌存储策略
     * 这里演示用内存存储令牌
     * 重启令牌失效，实际建议使用 数据库或JWT
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }


}
