package com.eip.sample.log.listener;

import com.eip.common.core.log.LogOperationDTO;
import com.eip.common.log.core.service.NativeLogListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 继承CustomLogListener类，可直接获取logDTO日志，不依赖于配置数据管道
 */
@Slf4j
@Component
public class LogCustomListener extends NativeLogListener {

    @Override
    public void createLog(LogOperationDTO logDTO) throws Exception {
        log.info("LogCustomListener 本地接收到日志 [{}]", logDTO);
    }
}
