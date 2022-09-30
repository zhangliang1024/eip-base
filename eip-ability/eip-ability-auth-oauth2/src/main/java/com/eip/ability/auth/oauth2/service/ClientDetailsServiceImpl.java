package com.eip.ability.auth.oauth2.service;

import cn.hutool.core.util.StrUtil;
import com.eip.ability.auth.oauth2.domain.OAuthClientDetails;
import com.eip.ability.auth.oauth2.mapper.ClientDetailsMapper;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: ClientDetailsServiceImpl
 * Function: 从数据库读取clientDetails相关配置
 * 1.有InMemoryClientDetailsService 和 JdbcClientDetailsService 两种方式选择
 * <p>
 * Date: 2022年09月22 15:27:29
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service
@RequiredArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final ClientDetailsMapper clientDetailsMapper;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        final OAuthClientDetails details = this.clientDetailsMapper.selectById(clientId);
        //if (Objects.isNull(details)) {
        //    throw new Auth2Exception("client_id 或 client_secret 错误", HttpStatus.BAD_REQUEST.value());
        //}
        //if (!details.getStatus()) {
        //    throw new Auth2Exception("client_id 已被禁用", HttpStatus.BAD_REQUEST.value());
        //}
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(details.getClientId());
        clientDetails.setClientSecret(passwordEncoder.encode(details.getClientSecret()));
        clientDetails.setAccessTokenValiditySeconds(details.getAccessTokenValidity());
        clientDetails.setRefreshTokenValiditySeconds(details.getRefreshTokenValidity());
        if (StringUtils.isNotBlank(details.getResourceIds())) {
            clientDetails.setResourceIds(StrUtil.split(details.getResourceIds(), ','));
        }
        if (StringUtils.isNotBlank(details.getAuthorizedGrantTypes())) {
            clientDetails.setAuthorizedGrantTypes(StrUtil.split(details.getAuthorizedGrantTypes(), ','));
        }
        if (StringUtils.isNotBlank(details.getScope())) {
            clientDetails.setScope(StrUtil.split(details.getScope(), ','));
        }
        if (StringUtils.isNotBlank(details.getAutoApprove())) {
            clientDetails.setAutoApproveScopes(StrUtil.split(details.getAutoApprove(), ','));
        }
        if (StringUtils.isNotBlank(details.getWebServerRedirectUri())) {
            final HashSet<String> registeredRedirectUris = Sets.newHashSet(StrUtil.split(details.getWebServerRedirectUri(), ','));
            clientDetails.setRegisteredRedirectUri(registeredRedirectUris);
        }
        if (StringUtils.isNotBlank(details.getAuthorities())) {
            List<SimpleGrantedAuthority> authorities = StrUtil.split(details.getAuthorities(), ',')
                    .stream().distinct().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            clientDetails.setAuthorities(authorities);
        }
        return clientDetails;
    }
}
