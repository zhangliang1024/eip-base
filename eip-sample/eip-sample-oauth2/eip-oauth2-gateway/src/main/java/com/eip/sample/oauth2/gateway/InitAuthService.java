package com.eip.sample.oauth2.gateway;

import com.eip.common.core.constants.AuthConstants;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * ClassName: InitAuthService
 * Function: uri和权限对应关系初始化到redis中
 * Date: 2022年01月18 13:32:47
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Service
@Deprecated
public class InitAuthService {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        redisTemplate.opsForHash().put(AuthConstants.OAUTH_URLS,"GET:/res/hello", Lists.newArrayList("ROLE_admin","ROLE_user"));
        redisTemplate.opsForHash().put(AuthConstants.OAUTH_URLS,"GET:/res/admin", Lists.newArrayList("ROLE_admin"));
    }
}
