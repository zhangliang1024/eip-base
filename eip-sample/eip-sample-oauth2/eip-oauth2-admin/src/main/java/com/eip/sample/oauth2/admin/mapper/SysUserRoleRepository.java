package com.eip.sample.oauth2.admin.mapper;

import com.eip.sample.oauth2.admin.model.po.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole,Long> {
    List<SysUserRole> findByUserId(Long userId);
}