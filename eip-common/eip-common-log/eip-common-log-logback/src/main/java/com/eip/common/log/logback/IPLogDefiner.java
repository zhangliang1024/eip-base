package com.eip.common.log.logback;

import ch.qos.logback.core.PropertyDefinerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * ClassName: IPLogDefiner
 * Function:
 * Date: 2021年12月31 15:17:47
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class IPLogDefiner extends PropertyDefinerBase {

    private static final Logger logger = LoggerFactory.getLogger(IPLogDefiner.class);

    @Override
    public String getPropertyValue() {
        return getUniqName();
    }

    private String getUniqName() {
        String localIp = null;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("[log-ip] - fail to get ip...", e);
        }
        String uniqName = UUID.randomUUID().toString().replace("-", "");
        if (localIp != null) {
            uniqName = localIp + "-" + uniqName;
        }
        return localIp;
    }
}
