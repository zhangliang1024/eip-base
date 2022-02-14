package com.eip.common.log.core.constant;

import org.apache.logging.log4j.util.Strings;

/**
 * ClassName: LogConstans
 * Function:
 * Date: 2022年02月11 19:05:16
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class LogConstans {

    //日志记录服务
    public final static String SAVE_AUDIT_URL = "http://audit-log/audit-log/bus/add";

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String ROCKET_MQ = "rocketMq";

    public static final String RABBIT_MQ = "rabbitMq";

    public static final String NATIVE = "native";

    public static final String STREAM = "stream";

    public static final String ROCKET_MQ_TOPIC = "logRecord";
    public static final String ROCKET_MQ_TAG = Strings.EMPTY;
    public static final String ROCKET_MQ_NAMESRV_ADDR = "localhost:9876";
    public static final String ROCKET_MQ_GROUP_NAME = "logRecord";
    public static final int ROCKET_MQ_MAXMESSAGE_SIZE = 4000000;
    public static final int ROCKET_MQ_SEND_MSG_TIMEOUT = 3000;
    public static final int ROCKET_MQ_RETRY_TIMES_WHEN_SEND_FAILD = 3;
}
