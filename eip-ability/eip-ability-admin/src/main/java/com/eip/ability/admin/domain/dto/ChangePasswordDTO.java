package com.eip.ability.admin.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 用户
 * </p>
 *
 * @author Levin
 * @since 2019-11-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(name = "ChangePasswordDTO", description = "用户")
public class ChangePasswordDTO implements Serializable {
    /**
     * 密码
     */
    @Schema(description = "旧密码")
    @NotEmpty(message = "旧密码不能为空")
    @Length(max = 64, message = "旧密码长度不能超过64")
    private String originalPassword;
    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotEmpty(message = "密码不能为空")
    @Length(max = 64, message = "密码长度不能超过64")
    private String password;

    /**
     * 密码
     */
    @Schema(description = "确认密码")
    @NotEmpty(message = "确认密码不能为空")
    @Length(max = 64, message = "确认密码长度不能超过64")
    private String confirmPassword;
}
