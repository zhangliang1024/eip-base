package com.eip.ability.admin.domain.entity.baseinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 实体类
 * 角色分配
 * 账号角色绑定
 * </p>
 *
 * @author Levin
 * @since 2019-10-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role_api")
@Schema(name= "RoleApi", description = "接口分配")
public class RoleApi {

    @Schema(description = "角色ID")
    @NotNull(message = "角色ID不能为空")
    @TableField("role_id")
    private Long roleId;

    @Schema(description = "接口ID")
    @NotNull(message = "接口ID不能为空")
    @TableField("api_id")
    private Long apiId;


}
