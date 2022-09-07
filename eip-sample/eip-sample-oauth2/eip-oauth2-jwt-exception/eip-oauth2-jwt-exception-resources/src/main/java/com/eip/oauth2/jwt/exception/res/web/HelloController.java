package com.eip.oauth2.jwt.exception.res.web;

import com.eip.oauth2.jwt.exception.res.model.LoginUser;
import com.eip.oauth2.jwt.exception.res.utils.AuthUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: HelloController
 * Function:
 * Date: 2022年01月17 17:59:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@RestController
public class HelloController {

    /**
     * 无权限拦截，认证成功都可以访问
     */
    @GetMapping("/hello")
    public String hello() {
        LoginUser user = AuthUserUtil.getCurrentUser();
        log.info("[REQUEST USER] - {}", user.getUsername());
        return "hello";
    }

    /**
     * ROLE_admin 的角色才可以访问
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_admin')")
    public String admin() {
        return "admin";
    }
}
