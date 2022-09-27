package com.eip.sample.sensitive.simple.domain;

import com.eip.common.core.sensitive.Sensitive;
import com.eip.common.core.sensitive.SensitiveTypeEnum;
import com.eip.sample.sensitive.simple.annotation.Desensitization;
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

    @Sensitive(prefixNoMaskLen = 2,suffixNoMaskLen = 2,symbol = "#")
    private String email;

    @Desensitization(index = {2, 3}, size = 3)
    private String remark;

    public static User mock(String username) {
        return User.builder()
                .userId(10000)
                .userName(username)
                .email("xxxx@124.com")
                .remark("你就说吧要则呢么半")
                .build();
    }


}
