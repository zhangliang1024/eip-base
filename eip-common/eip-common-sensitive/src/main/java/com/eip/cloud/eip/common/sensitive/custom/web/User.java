package com.eip.cloud.eip.common.sensitive.custom.web;

import com.eip.cloud.eip.common.sensitive.custom.annotation.Desensitization;
import com.eip.cloud.eip.common.sensitive.custom.annotation.Sensitive;
import com.eip.cloud.eip.common.sensitive.custom.enums.SensitiveTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * ClassName: User
 * Function:
 * Date: 2022年07月21 13:42:26
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Builder
public class User {

    private Integer userId;

    @Sensitive(type = SensitiveTypeEnum.NAME)
    private String userName;

    private String email;

    //@Sensitive(prefixNoMaskLen = 2,suffixNoMaskLen = 2,symbol = "#")
    @Desensitization(index = {2, 3}, size = 3)
    private String remark;


}
