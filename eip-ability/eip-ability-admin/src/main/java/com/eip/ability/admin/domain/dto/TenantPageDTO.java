package com.eip.ability.admin.domain.dto;

import com.eip.ability.admin.domain.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Levin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantPageDTO extends PageRequest {

    private String name;
    private String code;
    private Integer type;
    private Integer status;
    private Integer industry;

    private Long provinceId;
    private Long cityId;
    private Long districtId;
}
