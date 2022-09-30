package com.eip.ability.admin.domain.dto;

import com.eip.ability.admin.domain.enums.DataScopeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ClassName: ApiPermDTO
 * Function:
 * Date: 2022年09月28 10:59:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(name = "ApiPermDTO", description = "接口")
public class ApiPermDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long tenantId;

    @Schema(description = "接口名称")
    @NotEmpty(message = "接口名称不能为空")
    @Length(max = 30, message = "接口名称长度不能超过30")
    private String interfaceName;

    @Schema(description = "接口方法")
    @Length(max = 10, message = "接口方法长度不能超过20")
    private String interfaceMethod;

    @Schema(description = "接口地址")
    @NotEmpty(message = "接口地址不能为空")
    private String interfacePath;

    @Schema(description = "接口描述")
    @Length(max = 100, message = "接口描述长度不能超过100")
    private String interfaceDescription;


    @Schema(description = "状态")
    private Boolean locked;

    @Schema(description = "内置角色")
    private Boolean readonly;
    /**
     * 数据权限类型
     * #DataScopeType{ALL:1,全部;THIS_LEVEL:2,本级;THIS_LEVEL_CHILDREN:3,本级以及子级;CUSTOMIZE:4,自定义;SELF:5,个人;}
     */
    @Schema(description = "数据权限类型")
    @NotNull(message = "数据权限类型不能为空")
    private DataScopeType scopeType;
}
