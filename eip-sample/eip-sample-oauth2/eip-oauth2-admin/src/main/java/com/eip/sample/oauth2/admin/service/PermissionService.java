package com.eip.sample.oauth2.admin.service;


import com.eip.sample.oauth2.admin.model.vo.SysRolePermissionVO;

import java.util.List;

public interface PermissionService {
    /**
     * 获取所有的url->角色对应关系
     * @return
     */
    List<SysRolePermissionVO> listRolePermission();
}