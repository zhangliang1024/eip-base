package com.eip.common.core.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ClassName: AuthUserDetail
 * Function:
 * Date: 2022年02月14 13:55:45
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthUserDetail implements Serializable {

    private String userId;

    private String username;
    
    private String tenantCode; 
    
    private String tenantId; 


}
