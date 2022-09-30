package com.eip.ability.admin.domain.dto;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * ClassName: LoginDTO
 * Function:
 * Date: 2022年09月28 10:59:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@Accessors(chain = true)
public class LoginDTO {

    private Long userId;

    private String username;

    private String password;

    private String tenantCode;

    private String phone;

    private String verifyCode;

}
