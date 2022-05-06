package com.eip.ability.auth.web.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.eip.ability.auth.web.mapper.SysRoleRepository;
import com.eip.ability.auth.web.mapper.SysUserRepository;
import com.eip.ability.auth.web.mapper.SysUserRoleRepository;
import com.eip.ability.auth.web.model.po.SysUser;
import com.eip.ability.auth.web.model.po.SysUserRole;
import com.eip.common.core.constants.AuthConstants;
import com.eip.common.auth.com.model.SecurityUser;
import com.eip.common.auth.server.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ClassName: LoadUserService
 * Function:
 * Date: 2022年01月19 10:46:45
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service
public class LoadUserService implements AuthUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Override
    public SecurityUser loadUserByUsername(String username) {
        SysUser user = sysUserRepository.findByUsernameAndStatus(username, 1);
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("用户不存在！");
        //角色
        List<SysUserRole> sysUserRoles = sysUserRoleRepository.findByUserId(user.getId());
        //该用户的所有权限（角色）
        List<String> roles=new ArrayList<>();
        for (SysUserRole userRole : sysUserRoles) {
            sysRoleRepository.findById(userRole.getRoleId()).ifPresent(o-> roles.add(AuthConstants.ROLE_PREFIX+o.getCode()));
        }
        return SecurityUser.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                //将角色放入authorities中
                .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(roles,String.class)))
                .build();
    }
}
