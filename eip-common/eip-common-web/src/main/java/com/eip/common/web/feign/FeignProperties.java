package com.eip.common.web.feign;

import com.eip.common.web.interceptor.GlobalTraceLogIdInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * ClassName: FeignProperties
 * Function: 封装需要传递feign参数
 * Date: 2022年01月10 14:41:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@ConfigurationProperties(prefix = "eip.feign")
public class FeignProperties {

    private List<String> sendParamNames;

    public List<String> getSendParamNames() {
        sendParamNames.add(GlobalTraceLogIdInterceptor.GLOBAL_TRACE_ID);
        return sendParamNames;
    }
}
