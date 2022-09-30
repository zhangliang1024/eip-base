package com.eip.ability.admin.domain.entity.baseinfo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eip.ability.admin.domain.Entity;
import com.eip.ability.admin.domain.enums.DataScopeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author battcn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_api_perm")
public class ApiPerm extends Entity<Long> {

    private Long tenantId;

    @TableField("api_name")
    private String apiName;

    @TableField("api_method")
    private String apiMethod;

    @TableField("api_path")
    private String apiPath;

    @TableField("api_description")
    private String apiDescription;
    /**
     * 数据权限类型
     */
    private DataScopeType scopeType;

    private Boolean locked;

    private Boolean readonly;



}
