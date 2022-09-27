package com.eip.cloud.eip.common.sensitive.resolve;

import org.springframework.web.method.HandlerMethod;

/**
 * ClassName: HandlerMethodResolver
 * Function:
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface HandlerMethodResolver {

    HandlerMethod resolve();
}
