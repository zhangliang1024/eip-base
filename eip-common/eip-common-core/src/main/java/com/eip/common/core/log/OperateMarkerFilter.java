package com.eip.common.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Marker;

import java.util.Objects;

/**
 * ClassName: OperateMarkerFilter
 * Function: 操作日志记录
 * Date: 2022年02月14 13:32:55
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class OperateMarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

    private String marker;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.DENY;
        }
        Marker marker = event.getMarker();
        if (Objects.isNull(marker)) {
            return FilterReply.DENY;
        }

        if (marker.contains(marker)) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }

    @Override
    public void start() {
        if (StringUtils.isNotBlank(marker)) {
            super.start();
        }
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }
}
