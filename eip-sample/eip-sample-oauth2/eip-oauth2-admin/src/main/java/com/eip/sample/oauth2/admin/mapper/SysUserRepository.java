package com.eip.sample.oauth2.admin.mapper;

import com.eip.sample.oauth2.admin.model.po.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Long> {
    SysUser findByUsernameAndStatus(String username, Integer status);
}