package com.eip.ability.log.business;

import com.eip.ability.log.business.service.ILogBusinessService;
import com.eip.common.core.log.LogOperationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "日志操作接口")
@RestController
public class LogBusinessController {

    @Autowired
    private ILogBusinessService businessService;

    @ApiOperation("保存业务操作日志")
    @PostMapping("bussiness-log/save")
    public void saveLog(@RequestBody LogOperationDTO operationDTO) {
        businessService.saveLog(operationDTO);
    }


}
