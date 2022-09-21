package com.eip.ability.admin.mybatis;

import com.eip.ability.admin.util.SecurityUtils;
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
                return SecurityUtils.getAuthInfo().getTenantId();
            }

            @Override
            public Long userId() {
                return SecurityUtils.getAuthInfo().getUserId();
            }

            @Override
            public String realName() {
                return SecurityUtils.getAuthInfo().getRealName();
            }

            @Override
            public boolean anonymous() {
                return SecurityUtils.anonymous();
            }
        };
    }

}
