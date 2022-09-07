package com.eip.sample.oauth2.admin.service;

import cn.hutool.core.collection.CollectionUtil;
import com.eip.common.core.constants.AuthConstants;
import com.eip.sample.oauth2.admin.model.po.SysRole;
import com.eip.sample.oauth2.admin.model.vo.SysRolePermissionVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoadRolePermissionService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PermissionService permissionService;

    @PostConstruct
    public void init() {
        //获取所有的权限
        List<SysRolePermissionVO> list = permissionService.listRolePermission();
        list.parallelStream().peek(k -> {
            List<String> roles = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(k.getRoles())) {
                for (SysRole role : k.getRoles()) {
                    roles.add(AuthConstants.ROLE_PREFIX + role.getCode());
                }
            }
            //放入Redis中
            redisTemplate.opsForHash().put(AuthConstants.OAUTH_URLS, k.getUrl(), roles);
        }).collect(Collectors.toList());

        initRedis();
    }

    private void initRedis() {
        redisTemplate.opsForHash().put(AuthConstants.OAUTH_URLS,"GET:/res/hello", Lists.newArrayList("ROLE_admin","ROLE_user"));
        redisTemplate.opsForHash().put(AuthConstants.OAUTH_URLS,"GET:/res/admin", Lists.newArrayList("ROLE_admin"));

    }

}