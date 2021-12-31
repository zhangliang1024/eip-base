package com.eip.base.business.web.controller.idem;

import com.eip.common.idempotent.annotation.AutoIdempotent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: IdemController
 * Function:
 * Date: 2021年12月07 17:04:05
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@RestController
@RequestMapping("idem")
public class IdemController {


    @AutoIdempotent
    @GetMapping("")
    public String idem(){
        return "idem succes";
    }

}
