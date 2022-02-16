package com.eip.common.web.base;

import com.eip.common.core.core.protocol.request.PageDTO;
import com.eip.common.core.core.protocol.response.ApiResult;
import com.eip.common.core.core.protocol.response.PageResult;
import com.eip.common.core.core.protocol.response.ResponseDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BaseController<Service extends BaseService, RequestDTO, Response extends ResponseDTO> {

    @Autowired
    protected Service service;

    @PostMapping("create")
    public ApiResult<Long> create(@RequestBody RequestDTO requestDTO) {
        Long id = service.create(requestDTO, "");
        return ApiResult.ok(id);
    }

    @GetMapping("show/{id}")
    public ApiResult<Response> show(@PathVariable Long id) {
        Response reponse = (Response) service.show(id);
        return ApiResult.ok(reponse);
    }

    @PostMapping("update")
    public ApiResult update(@RequestBody RequestDTO requestDTO) {
        service.modify(null, requestDTO);
        return ApiResult.ok();
    }

    @PostMapping("delete/{id}")
    public ApiResult remove(@PathVariable Long id) {
        service.delete(id);
        return ApiResult.ok();
    }

    @PostMapping("list")
    public ApiResult<PageResult> list(@RequestBody PageDTO page) {
        PageResult result = service.pageDTOList(page);
        return ApiResult.ok(result);
    }

}
