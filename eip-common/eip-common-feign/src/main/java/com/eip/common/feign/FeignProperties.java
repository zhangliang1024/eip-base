package com.eip.common.feign;

import com.eip.common.core.constants.GlobalConstans;
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
        sendParamNames.add(GlobalConstans.GLOBAL_TRACE_ID);
        return sendParamNames;
    }
}
