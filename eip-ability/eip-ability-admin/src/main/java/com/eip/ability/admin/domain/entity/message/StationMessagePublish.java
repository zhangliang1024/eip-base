package com.eip.ability.admin.domain.entity.message;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eip.ability.admin.domain.SuperEntity;
import com.eip.ability.admin.domain.enums.ReceiverType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Levin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_station_message_publish")
public class StationMessagePublish extends SuperEntity<Long> {

    @TableField("`title`")
    private String title;

    @TableField("`level`")
    private String level;

    @TableField("`status`")
    private Boolean status;

    @TableField("`type`")
    private ReceiverType type;

    @TableField("`receiver`")
    private String receiver;

    @TableField("`description`")
    private String description;

    @TableField("`content`")
    private String content;

}
