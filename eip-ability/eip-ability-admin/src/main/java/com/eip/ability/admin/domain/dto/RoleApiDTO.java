package com.eip.ability.admin.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 实体类
 * 角色的资源
 * </p>
 *
 * @author Levin
 * @since 2019-07-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(name= "RoleApiDTO", description = "角色的接口")
public class RoleApiDTO implements Serializable {


    /**
     * 接口ID
     */
    @Schema(description = "接口ID")
    private List<Long> apiIdList;

    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

}
