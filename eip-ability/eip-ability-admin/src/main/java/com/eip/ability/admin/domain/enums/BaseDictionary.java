package com.eip.ability.admin.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Levin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseDictionary {

    private String code;

    private String desc;

}
