package com.eip.common.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Marker;

import java.util.Objects;

/**
 * ClassName: IgnoreMarkerFilter
 * Function: 忽略日志记录
 * Date: 2022年02月14 13:12:29
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class IgnoreMarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

    private String marker;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.DENY; //拒绝，删除日志事件，不调用下游过滤器
        }
        Marker marker = event.getMarker();
        if (Objects.isNull(marker)) {
            return FilterReply.NEUTRAL; //中立 继续调用下一个过滤器，若没有则正常处理记录日志事件
        }

        if (marker.contains(marker)) {
            return FilterReply.ACCEPT; //通过，跳过其余的过滤器调用
        } else {
            return FilterReply.NEUTRAL;
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
