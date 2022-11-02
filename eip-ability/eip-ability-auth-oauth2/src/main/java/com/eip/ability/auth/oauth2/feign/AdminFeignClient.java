package com.eip.ability.auth.oauth2.feign;

import com.eip.common.auth.core.domain.UserInfoDetails;
import com.eip.common.core.core.protocol.response.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: AdminFeignClient
 * Function:
 * Date: 2022年09月28 14:35:05
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@FeignClient(name = "wemirr-platform-authority")
public interface AdminFeignClient {

    @PostMapping("users/username")
    ApiResult<UserInfoDetails> loadUserByUsername(@RequestParam("username") String username, @RequestParam("tenantCode") String tenantCode);
}
