package com.eip.ability.auth.web.mapper;

import com.eip.ability.auth.web.model.po.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Long> {
    SysUser findByUsernameAndStatus(String username,Integer status);
}