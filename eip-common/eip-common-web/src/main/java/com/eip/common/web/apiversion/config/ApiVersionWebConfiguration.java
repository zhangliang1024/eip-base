package com.eip.common.web.apiversion.config;

import com.eip.common.web.apiversion.mappinghandler.ApiVersionHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * ClassName: ApiVersionWebConfiguration
 * Function: 自定义handlerMappging加入spring启动链路中
 * Date: 2022年01月10 13:30:17
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class ApiVersionWebConfiguration implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionHandlerMapping();
    }
}
