package com.eip.ability.admin.domain.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eip.ability.admin.domain.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * <p>
 * 实体类
 * 字典类型
 * </p>
 *
 * @author Levin
 * @since 2020-01-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("common_dictionary")
@Schema(name= "Dictionary", description = "字典类型")
public class Dictionary extends SuperEntity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @Schema(description = "编码")
    @TableField(value = "`code`", condition = LIKE)
    private String code;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @TableField(value = "name", condition = LIKE)
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @TableField(value = "description", condition = LIKE)
    private String description;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Boolean status;

    @TableField(value = "`readonly`")
    private Boolean readonly;

    @TableField(value = "`sequence`")
    private Integer sequence;

}
