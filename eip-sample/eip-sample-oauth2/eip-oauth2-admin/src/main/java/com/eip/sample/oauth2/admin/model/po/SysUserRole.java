package com.eip.sample.oauth2.admin.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * ClassName: SysUserRole
 * Function:
 * Date: 2022年01月19 10:41:26
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Entity
@Table(name = "sys_user_role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;


}
