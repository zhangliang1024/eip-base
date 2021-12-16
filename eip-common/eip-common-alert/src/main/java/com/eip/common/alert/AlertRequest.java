package com.eip.common.alert;

import lombok.Data;

/**
 * ClassName: AlertRequest
 * Function: 告警请求
 * Date: 2021年12月15 10:12:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class AlertRequest {

    /**
     * 类型
     */
    private String type;
    /**
     * 密钥
     */
    private String secretKey;
    /**
     * 接受者
     */
    private String receives;
    /**
     * 内容
     */
    private AlertBasetDTO basetDTO;

}
