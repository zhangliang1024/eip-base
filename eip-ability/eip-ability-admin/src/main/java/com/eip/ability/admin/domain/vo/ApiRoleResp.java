package com.eip.ability.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Levin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRoleResp {

    private List<ApiRoleDetail> apiRoleDetails;
    private List<Long> originTargetKeys;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiRoleDetail {
        private Long id;

        private String apiName;

        private String apiMethod;

        private String apiPath;

    }

}
