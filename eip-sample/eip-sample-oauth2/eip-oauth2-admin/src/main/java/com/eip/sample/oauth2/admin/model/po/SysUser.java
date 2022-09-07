package com.eip.sample.oauth2.admin.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * ClassName: SysUser
 * Function: 用户实体类
 * Date: 2022年01月19 10:36:39
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */

@Data
@Entity
@Table(name = "sys_user")

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    private String userId;

    private String username;

    private String nickname;

    private Integer gender;

    private String avatar;

    private String password;

    private String mobile;

    private String email;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
