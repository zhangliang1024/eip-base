package com.eip.ability.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.eip.ability.admin.domain.DataScope;
import com.eip.ability.admin.domain.dto.UserSaveDTO;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.entity.baseinfo.User;
import com.eip.ability.admin.domain.entity.baseinfo.UserRole;
import com.eip.ability.admin.domain.entity.tenant.Tenant;
import com.eip.ability.admin.domain.vo.UserResp;
import com.eip.ability.admin.domain.vo.UserVO;
import com.eip.ability.admin.exception.CheckedException;
import com.eip.ability.admin.mapper.*;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.mybatis.wraps.query.LbqWrapper;
import com.eip.ability.admin.oauth2.entity.UserInfoDetails;
import com.eip.ability.admin.oauth2.exception.Auth2Exception;
import com.eip.ability.admin.service.UserService;
import com.eip.common.core.redis.RedisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements UserService {

    private static final String PHONE_REGEX = "^[1][0-9]{10}$";
    private final UserMapper userMapper;
    private final TenantMapper tenantMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;
    private final ResourceMapper resourceMapper;
    private final RedisService redisService;


    @Override
    public void addUser(UserSaveDTO dto) {
        final long count = super.count(Wraps.<User>lbQ().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw CheckedException.badRequest("账号已存在");
        }
        final User user = BeanUtil.toBean(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        super.save(user);
    }

    @Override
    public List<User> list(DataScope scope) {
        return baseMapper.list(scope);
    }

    @Override
    public IPage<UserResp> findPage(IPage<User> page, LbqWrapper<User> wrapper) {
        return baseMapper.findPage(page, wrapper);
    }

    @Override
    public void changePassword(Long userId, String orgPassword, String newPassword) {
        final User user = Optional.ofNullable(this.baseMapper.selectById(userId))
                .orElseThrow(() -> CheckedException.notFound("用户不存在"));
        if (!passwordEncoder.matches(orgPassword, user.getPassword())) {
            throw CheckedException.badRequest("原始密码错误");
        }
        User record = new User();
        record.setId(userId);
        record.setPassword(passwordEncoder.encode(newPassword));
        this.userMapper.updateById(record);
    }

    @Override
    @DSTransactional
    public void deleteById(Long id) {
        final User user = Optional.ofNullable(getById(id)).orElseThrow(() -> CheckedException.notFound("用户不存在"));
        if (user.getReadonly()) {
            throw CheckedException.badRequest("内置用户不允许删除");
        }
        baseMapper.deleteById(id);
        userRoleMapper.delete(Wraps.<UserRole>lbQ().eq(UserRole::getUserId, id));
    }

    @Override
    public UserInfoDetails loadUserByUsername(String username, String tenantCode) {
        if (StringUtils.isBlank(username)) {
            throw new Auth2Exception("账号名不能为空");
        }
        if (StringUtils.isBlank(tenantCode)) {
            throw new Auth2Exception("租户编码不能为空");
        }

        final Tenant tenant = Optional.ofNullable(
                tenantMapper.selectOne(Wraps.<Tenant>lbQ().eq(Tenant::getCode, tenantCode)))
                .orElseThrow(() -> CheckedException.notFound("{0}租户不存在", tenantCode));
        if (tenant.getLocked()) {
            throw CheckedException.badRequest("租户已被禁用,请联系管理员");
        }
        final User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getTenantId, tenant.getId()).eq(User::getUsername, username));
        if (Objects.isNull(user)) {
            throw CheckedException.notFound("账户不存在");
        }

        String userKey = getKey("user", String.valueOf(user.getId()));
        this.redisService.set(userKey, JSONUtil.toJsonStr(user));

        final UserInfoDetails info = new UserInfoDetails();
        info.setTenantCode(tenantCode);
        info.setTenantId(user.getTenantId());
        info.setUserId(user.getId());
        info.setUsername(username);
        info.setRealName(user.getNickName());
        info.setNickName(user.getNickName());
        info.setMobile(user.getMobile());
        info.setEmail(user.getEmail());
        info.setDescription(user.getDescription());
        info.setSex(Objects.isNull(user.getSex()) ? null : user.getSex().getValue());
        info.setEnabled(user.getStatus());
        info.setAvatar(user.getAvatar());
        info.setPassword(user.getPassword());

        setAuthorize(info);
        return info;
    }

    /**
     * 设置授权信息
     *
     * @param user user
     */
    private void setAuthorize(UserInfoDetails user) {
        final List<String> permissions = Optional.ofNullable(this.resourceMapper.queryPermissionByUserId(user.getUserId())).orElseGet(Lists::newArrayList);
        String permKey = getKey("perm", String.valueOf(user.getUserId()));
        this.redisService.set(permKey, JSONUtil.toJsonStr(permissions));

        //final List<String> roles = Optional.ofNullable(this.roleMapper.findRoleByUserId(user.getUserId())).orElseGet(Lists::newArrayList)
        //        .stream().map(Role::getCode).collect(toList());
        //// 验证角色和登录系统
        //Set<String> authorize = Sets.newHashSet();
        //authorize.addAll(roles);
        //authorize.addAll(permissions);
        //user.setRoles(roles);
        //user.setPermissions(permissions);
        //user.setAuthorities(authorize.stream().filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
    }


    private String getKey(String prefix, String key) {
        return prefix + ":" + key;
    }


    @Override
    public UserInfoDetails getUser(Long id) {
        final User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        final Tenant tenant = tenantMapper.selectOne(Wraps.<Tenant>lbQ().eq(Tenant::getId, user.getTenantId()));

        final UserInfoDetails info = new UserInfoDetails();
        info.setTenantCode(tenant.getCode());
        info.setTenantId(user.getTenantId());
        info.setUserId(user.getId());
        info.setUsername(user.getUsername());
        info.setRealName(user.getNickName());
        info.setNickName(user.getNickName());
        info.setMobile(user.getMobile());
        info.setEmail(user.getEmail());
        info.setDescription(user.getDescription());
        info.setSex(Objects.isNull(user.getSex()) ? null : user.getSex().getValue());
        info.setEnabled(user.getStatus());
        info.setAvatar(user.getAvatar());
        info.setPassword(user.getPassword());

        setAuthorize1(info);
        return info;
    }

    /**
     * 设置授权信息
     *
     * @param user user
     */
    private void setAuthorize1(UserInfoDetails user) {
        final List<String> permissions = Optional.ofNullable(this.resourceMapper.queryPermissionByUserId(user.getUserId())).orElseGet(Lists::newArrayList);
        String permKey = getKey("perm", String.valueOf(user.getUserId()));
        this.redisService.set(permKey, JSONUtil.toJsonStr(permissions));

        final List<String> roles = Optional.ofNullable(this.roleMapper.findRoleByUserId(user.getUserId())).orElseGet(Lists::newArrayList)
                .stream().map(Role::getCode).collect(toList());
        // 验证角色和登录系统
        Set<String> authorize = Sets.newHashSet();
        authorize.addAll(roles);
        authorize.addAll(permissions);
        user.setRoles(roles);
        user.setPermissions(permissions);
        user.setAuthorities(authorize.stream().filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
    }
}
