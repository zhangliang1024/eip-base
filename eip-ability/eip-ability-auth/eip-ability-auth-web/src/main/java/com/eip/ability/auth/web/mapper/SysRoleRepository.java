package com.eip.ability.auth.web.mapper;

import com.eip.ability.auth.web.model.po.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository  extends JpaRepository<SysRole,Long> {
}