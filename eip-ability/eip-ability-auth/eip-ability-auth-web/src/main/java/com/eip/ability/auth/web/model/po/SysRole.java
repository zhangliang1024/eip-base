package com.eip.ability.auth.web.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * ClassName: SysRole
 * Function: 角色实体类
 * Date: 2022年01月19 10:40:42
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Entity
@Table(name = "sys_role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    private String name;

    private String code;

    private Integer status;

    private Date createTime;

    private Date updateTime;


}
