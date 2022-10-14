package com.eip.common.auth.core.config;

import com.eip.common.auth.core.properties.SecurityJwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;

/**
 * ClassName: AuthConfiguration
 * Function: 令牌相关配置
 * Date: 2022年01月18 09:55:22
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RequiredArgsConstructor
@Import({SecurityJwtProperties.class})
public class AuthConfiguration {


}
