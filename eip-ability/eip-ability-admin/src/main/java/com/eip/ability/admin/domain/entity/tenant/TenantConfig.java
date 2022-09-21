package com.eip.ability.admin.domain.entity.tenant;


import com.baomidou.mybatisplus.annotation.TableName;
import com.eip.ability.admin.domain.SuperEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租户配置信息
 * </p>
 *
 * @author Levin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_tenant_config")
public class TenantConfig extends SuperEntity<Long> {

    private Long tenantId;
    private Long dynamicDatasourceId;

}
