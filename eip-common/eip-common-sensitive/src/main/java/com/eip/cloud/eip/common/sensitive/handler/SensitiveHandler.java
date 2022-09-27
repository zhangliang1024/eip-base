package com.eip.cloud.eip.common.sensitive.handler;

/**
 * ClassName: SensitiveHandler
 * Function: 数据脱密处理
 * Date: 2022年09月27 13:52:23
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface SensitiveHandler {

    /**
     * 默认数据脱密处理
     */
    String handler(SensitiveWrapper sensitiveWrapper);

    /**
     * 自定义数据脱密处理
     */
    String customHandler(SensitiveWrapper sensitiveWrapper);
}
