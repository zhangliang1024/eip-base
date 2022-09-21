package com.eip.ability.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.DataScope;
import com.eip.ability.admin.domain.dto.UserSaveDTO;
import com.eip.ability.admin.domain.entity.baseinfo.User;
import com.eip.ability.admin.domain.vo.UserResp;
import com.eip.ability.admin.mybatis.wraps.query.LbqWrapper;
import com.eip.ability.admin.mybatis.supers.SuperService;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 账号
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
public interface UserService extends SuperService<User> {


    /**
     * 添加用户
     *
     * @param dto 用户信息
     */
    void addUser(UserSaveDTO dto);

    /**
     * 根据条件查询
     *
     * @param scope scope
     * @return 查询结果
     */
    List<User> list(DataScope scope);

    /**
     * 数据权限 分页
     *
     * @param page    page
     * @param wrapper wrapper
     * @return 查询结果
     */
    IPage<UserResp> findPage(IPage<User> page, LbqWrapper<User> wrapper);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param orgPassword 原始密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String orgPassword, String newPassword);

    /**
     * 根据ID删除用户
     *
     * @param id id
     */
    void deleteById(Long id);
}
