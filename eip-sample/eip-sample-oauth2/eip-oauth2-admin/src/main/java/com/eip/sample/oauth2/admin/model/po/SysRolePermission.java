package com.eip.sample.oauth2.admin.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * ClassName: SysRolePermission
 * Function:
 * Date: 2022年01月19 10:42:26
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Entity
@Table(name = "sys_role_permission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;

}
