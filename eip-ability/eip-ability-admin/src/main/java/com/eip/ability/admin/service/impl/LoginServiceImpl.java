package com.eip.ability.admin.service.impl;

import com.eip.ability.admin.controller.TokenInfo;
import com.eip.ability.admin.domain.key.RedisTokenKey;
import com.eip.ability.admin.domain.dto.LoginDTO;
import com.eip.ability.admin.domain.vo.LoginVO;
import com.eip.ability.admin.configuration.properties.SecurityProperties;
import com.eip.ability.admin.service.ILoginService;
import com.eip.ability.admin.service.LoginLogService;
import com.eip.ability.admin.service.VerificationService;
import com.eip.common.core.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.eip.common.core.constants.AuthConstants.*;

/**
 * ClassName: LoginServiceImpl
 * Function:
 * Date: 2022年09月28 11:07:52
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final VerificationService verificationService;
    private final SecurityProperties properties;
    private final LoginLogService loginLogService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisService redisService;
    private final RedisTokenKey redisTokenKey;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 校验验证码
        String verifyCode = loginDTO.getVerifyCode();
        this.verificationService.valid(loginDTO.getVerifyKey(), verifyCode);

        // 获取token
        TokenInfo result = this.getToken(loginDTO);
        Long userId = result.getUser_id();
        Long expireTime = result.getExpires_in();
        String accessToken = result.getAccess_token();
        String refreshToken = result.getRefresh_token();

        // 缓存token TODO refresh_token过期时间有问题
        String accessKey = redisTokenKey.getAccessToken(String.valueOf(userId));
        String refreshKey = redisTokenKey.getRefreshToken(String.valueOf(userId));
        this.redisService.set(accessKey, accessToken, expireTime - 3);
        this.redisService.set(refreshKey, accessToken, expireTime - 3);

        // 写个登录日志
        this.loginLogService.saveLoginLog(userId, loginDTO.getUsername(), "");
        return LoginVO.builder().userId(userId).accessToken(accessToken).refreshToken(refreshToken).build();
    }


    public TokenInfo getToken(LoginDTO loginDTO) {
        String accessTokenUri = this.properties.getAccessTokenUri();
        String clientId = this.properties.getClientId();
        String clientSecret = this.properties.getClientSecret();

        String tenantCode = loginDTO.getTenantCode();
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);
        headers.set(OAUTH2_HEARDE_TENANT_CODE, tenantCode);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(OAUTH2_USERNAME, username);
        params.add(OAUTH2_PASSWORD, password);
        params.add(OAUTH2_GRANT_TYPE, OAUTH2_PASSWORD_GRANT_TYPE);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> result = this.restTemplate.exchange(accessTokenUri, HttpMethod.POST, entity, TokenInfo.class);
        if (result.getStatusCode().value() != 200) {
            throw new RuntimeException("request token error");
        }

        return result.getBody();
    }


    @Override
    public void logout(LoginDTO loginDTO) {
        Long userId = loginDTO.getUserId();

        String accessKey = redisTokenKey.getAccessToken(String.valueOf(userId));
        String refreshKey = redisTokenKey.getRefreshToken(String.valueOf(userId));
        this.redisService.del(accessKey, String.valueOf(userId));
        this.redisService.del(refreshKey, String.valueOf(userId));
    }


}
