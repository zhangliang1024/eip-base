package com.eip.sample.log.controller;

import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.sample.log.domain.LogDTO;
import com.eip.sample.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/success")
    public LogDTO testSuccess() throws Exception {
        LogDTO log = new LogDTO();
        log.setId(1L);
        log.setStr("str");
        log.setList(Arrays.asList("1","2","3"));
        return logService.serviceFunc(log, true);
    }

    @GetMapping("/failure")
    public ApiResult<LogDTO> testFailure() throws Exception {
        LogDTO log = new LogDTO();
        log.setId(2L);
        log.setStr("str");
        log.setList(Arrays.asList("1","2","3"));
        return ApiResult.ok(logService.serviceFunc(log, false));
    }

}
