package com.eip.base.business.web.controller.lock;

import com.eip.base.lock.annotation.CLock;
import com.eip.base.lock.core.RedissonLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Barnett
 * @Date: 2021/11/24 13:13
 * @Version: V1.0.0
 * @Description:
 */
@Api(tags = "分布式锁")
@RestController
@RequestMapping("lock")
public class LockController {



    @Autowired
    private RedissonLock lock;


    @GetMapping("{id}")
    @ApiOperation(value = "获取分布式锁")
    public Object lock(@PathVariable long id){
        boolean bool = lock.tryLock("num" + String.valueOf(id));
        System.out.println(bool);
        return bool;
    }

    @GetMapping("aop/{id}")
    @ApiOperation(value = "AOP获取分布式锁")
    //@CacheEvict(key = "#root.targetClass + ':listByUserId:' + #userId")
    @CLock(key = "#id")
    public Object lockAop(HttpServletRequest request,@PathVariable long id){
        String authorization = request.getHeader("Authorization");
        return authorization;
    }

}
