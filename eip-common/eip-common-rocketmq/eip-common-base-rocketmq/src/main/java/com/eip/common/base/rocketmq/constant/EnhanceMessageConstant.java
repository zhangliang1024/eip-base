package com.eip.common.base.rocketmq.constant;

import org.apache.logging.log4j.util.Strings;

/**
 * ClassName: EnhanceMessageConstant
 * Function:
 * Date: 2023年07月20 15:12:02
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class EnhanceMessageConstant {

    /**
     * 重试消息来源标识
     */
    public static final String RETRY_PREFIX = "RETRY_";
    /**
     * 延时等级
     * 发送延时消息（同步发送消息，delayLevel的值就为0，因为不延时）
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    public static final int FIVE_SECOND = 5;
    /**
     * 默认灰度标识：""
     */
    public static final String DEFAULT_GRAY_FLAG = Strings.EMPTY;
}
