package com.eip.ability.admin.service.impl;

import com.eip.ability.admin.controller.TokenInfo;
import com.eip.ability.admin.domain.dto.LoginDTO;
import com.eip.ability.admin.domain.vo.LoginVO;
import com.eip.ability.admin.oauth2.properties.SecurityIgnoreProperties;
import com.eip.ability.admin.service.ILoginService;
import com.eip.ability.admin.service.LoginLogService;
import com.eip.ability.admin.service.VerificationService;
import com.eip.common.core.constants.AuthConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

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
    private final SecurityIgnoreProperties properties;
    private final LoginLogService loginLogService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate redisTemplate;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 校验 验证码
        String verifyCode = loginDTO.getVerifyCode();
        //verificationService.valid("", verifyCode);

        // 获取token
        TokenInfo result = getToken(loginDTO);
        Long userId = result.getUser_id();
        Long expireTime = result.getExpires_in();
        String accessToken = result.getAccess_token();
        String refreshToken = result.getRefresh_token();

        redisTemplate.opsForValue().set(getKey("accessToken", String.valueOf(userId)), accessToken, expireTime - 3);
        redisTemplate.opsForValue().set(getKey("refreshToken", String.valueOf(userId)), accessToken, expireTime - 3);

        // 写个登录日志
        //this.loginLogService.saveLoginLog(userId, loginDTO.getUsername(), "");

        //礼记 殷商的时候 被砍之前 大声喊叫 嚎叫 昭告于天地之间 猪牛 道观 铜板 我送钱来了 礼仪性的洗洗 杀人献祭
        return LoginVO.builder().userId(userId).accessToken(accessToken).refreshToken(refreshToken).build();
    }


    public TokenInfo getToken(LoginDTO loginDTO) {
        String accessTokenUri = properties.getAccessTokenUri();
        String clientId = properties.getClientId();
        String clientSecret = properties.getClientSecret();

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

        ResponseEntity<TokenInfo> result = restTemplate.exchange(accessTokenUri, HttpMethod.POST, entity, TokenInfo.class);
        if (result.getStatusCode().value() != 200) {
            throw new RuntimeException("request token error");
        }

        return result.getBody();
    }


    @Override
    public void logout(LoginDTO loginDTO) {
        Long userId = loginDTO.getUserId();
        redisTemplate.delete(getKey("accessToken", String.valueOf(userId)));
        redisTemplate.delete(getKey("refreshToken", String.valueOf(userId)));
    }


    public String getKey(String prefix, String key) {
        return prefix + ":" + key;
    }
}
