package com.eip.common.core.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ClassName: LogOperationDTO
 * Function: 日志记录
 * Date: 2022年02月11 11:15:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LogOperationDTO {
    /**
     * 唯一ID
     */
    private String logId;
    /**
     * 业务唯一ID
     */
    private String businessId;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 注解中传递的msg (操作内容)
     */
    private String logMessage;
    /**
     * 事件级别;1高2中3低
     */
    private String eventLevel;
    /**
     * 操作模块
     */
    private String operateModule;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 请求者物理地址
     */
    private String macAddress;
    /**
     * 请求路径
     */
    private String requestUrl;
    /**
     * 子系统
     */
    private String operateSubSystem;
    /**
     * 操作人账号
     */
    private String operator;
    /**
     * 函数是否执行成功
     */
    private Boolean success;
    /**
     * 方法执行成功后的返回值（JSON）
     */
    private String result;
    /**
     * 函数执行失败时写入异常信息
     */
    private String exceptionMsg;
    /**
     * 操作执行时间
     */
    private Date operateTime;
    /**
     * 方法执行耗时（毫秒）
     */
    private Long executionTime;

}
