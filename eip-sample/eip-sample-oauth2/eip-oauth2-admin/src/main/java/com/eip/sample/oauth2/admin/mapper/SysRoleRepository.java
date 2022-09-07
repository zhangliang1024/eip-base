package com.eip.sample.oauth2.admin.mapper;

import com.eip.sample.oauth2.admin.model.po.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository  extends JpaRepository<SysRole,Long> {
}