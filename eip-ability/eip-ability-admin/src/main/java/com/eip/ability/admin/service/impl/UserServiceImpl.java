package com.eip.ability.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.eip.ability.admin.domain.DataScope;
import com.eip.ability.admin.domain.UserInfoDetails;
import com.eip.ability.admin.domain.dto.UserSaveDTO;
import com.eip.ability.admin.domain.entity.baseinfo.ApiPerm;
import com.eip.ability.admin.domain.entity.baseinfo.Role;
import com.eip.ability.admin.domain.entity.baseinfo.User;
import com.eip.ability.admin.domain.entity.baseinfo.UserRole;
import com.eip.ability.admin.domain.entity.tenant.Tenant;
import com.eip.ability.admin.domain.key.RedisPermKey;
import com.eip.ability.admin.domain.key.RedisUserKey;
import com.eip.ability.admin.domain.vo.UserResp;
import com.eip.ability.admin.exception.AdminExceptionEnum;
import com.eip.ability.admin.exception.AdminRuntimeException;
import com.eip.ability.admin.mapper.*;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.mybatis.wraps.query.LbqWrapper;
import com.eip.ability.admin.service.UserService;
import com.eip.ability.admin.util.StringUtils;
import com.eip.common.core.redis.RedisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserMapper userMapper;
    private final TenantMapper tenantMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;
    private final ResourceMapper resourceMapper;
    private final ApiPermMapper apiPermMapper;
    private final RedisService redisService;
    private final RedisPermKey redisPermKey;
    private final RedisUserKey redisUserKey;


    @Override
    public void addUser(UserSaveDTO dto) {
        final long count = super.count(Wraps.<User>lbQ().eq(User::getUsername, dto.getUsername()));
        AdminExceptionEnum.USER_NAME_HAVED_EXIST.assertIsFalse(count > 0);

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
        final User user = Optional.ofNullable(this.baseMapper.selectById(userId)).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.USER_NOT_FOUNT.getMessage()));

        AdminExceptionEnum.USER_ORIGIN_PASSWORD_ERROR.assertIsFalse(!passwordEncoder.matches(orgPassword, user.getPassword()));

        User record = new User();
        record.setId(userId);
        record.setPassword(passwordEncoder.encode(newPassword));
        this.userMapper.updateById(record);
    }

    @Override
    @DSTransactional
    public void deleteById(Long id) {
        final User user = Optional.ofNullable(getById(id)).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.USER_NOT_FOUNT.getMessage()));

        AdminExceptionEnum.SYSTEM_USER_DONOT_DELETE.assertIsFalse(user.getReadonly());

        baseMapper.deleteById(id);
        userRoleMapper.delete(Wraps.<UserRole>lbQ().eq(UserRole::getUserId, id));
    }

    @Override
    public UserInfoDetails loadUserByUsername(String username, String tenantCode) {
        AdminExceptionEnum.USER_NAME_NOT_EMPTY.assertNotEmpty(username);
        AdminExceptionEnum.TENANT_CODE_NOT_EMPTY.assertNotEmpty(tenantCode);


        final Tenant tenant =
                Optional.ofNullable(tenantMapper.selectOne(Wraps.<Tenant>lbQ().eq(Tenant::getCode, tenantCode))).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.TENANT_NOT_FOUNT.getMessage()));

        AdminExceptionEnum.TENANT_HAS_BEEN_DISABLED.assertIsFalse(tenant.getLocked());

        final User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getTenantId, tenant.getId()).eq(User::getUsername, username));

        AdminExceptionEnum.SYSTEM_USER_NOT_FOUND.assertIsFalse(user.getReadonly());

        String userInfoKey = redisUserKey.getUserInfoKey(user.getId());
        this.redisService.set(userInfoKey, JSONUtil.toJsonStr(user));

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

        setApiPerm(info);
        return info;
    }

    /**
     * 授权接口访问
     */
    private void setApiPerm(UserInfoDetails user) {
        final List<ApiPerm> apiPerms = this.apiPermMapper.selectApiPermByUserId(user.getUserId());
        if (CollectionUtil.isNotEmpty(apiPerms)) {
            List<String> permissions = apiPerms.stream().map(e -> e.getApiMethod() + StringUtils.SIGN_MAO_HAO + e.getApiPath()).collect(Collectors.toList());
            String permKey = redisPermKey.getPermKey(user.getUserId());
            this.redisService.set(permKey, JSONUtil.toJsonStr(permissions));
        }
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
        final List<String> roles = Optional.ofNullable(this.roleMapper.findRoleByUserId(user.getUserId())).orElseGet(Lists::newArrayList).stream().map(Role::getCode).collect(toList());
        // 验证角色和登录系统
        Set<String> authorize = Sets.newHashSet();
        authorize.addAll(roles);
        authorize.addAll(permissions);
        user.setRoles(roles);
        user.setPermissions(permissions);
        user.setAuthorities(authorize.stream().filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
    }
}
