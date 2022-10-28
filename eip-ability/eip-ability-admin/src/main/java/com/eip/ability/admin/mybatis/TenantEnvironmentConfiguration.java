package com.eip.ability.admin.mybatis;

import com.eip.ability.admin.util.SecurityUtils;
import com.eip.common.core.auth.AuthUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Levin
 */
@Configuration
public class TenantEnvironmentConfiguration {

    @Bean
    public TenantEnvironment tenantEnvironment() {
        return new TenantEnvironment() {
            @Override
            public Long tenantId() {
                return Long.parseLong(AuthUserContext.get().getTenantId());
            }

            @Override
            public Long userId() {
                return Long.parseLong(AuthUserContext.get().getUserId());
            }

            @Override
            public String realName() {
                return AuthUserContext.get().getUsername();
            }

            @Override
            public boolean anonymous() {
                return SecurityUtils.anonymous();
            }
        };
    }

}
