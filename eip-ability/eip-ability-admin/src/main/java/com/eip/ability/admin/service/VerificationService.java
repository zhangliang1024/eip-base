package com.eip.ability.admin.service;

import com.eip.common.core.core.protocol.response.ApiResult;
import com.wf.captcha.base.Captcha;

/**
 * @author Levin
 */
public interface VerificationService {

    /**
     * 创建验证码
     *
     * @param key key
     * @return 验证码结果
     */
    Captcha create(String key);

    /**
     * 验证图形验证码
     *
     * @param key   key
     * @param value val
     * @return 验证结果
     */
    ApiResult<Boolean> valid(String key, String value);

}
