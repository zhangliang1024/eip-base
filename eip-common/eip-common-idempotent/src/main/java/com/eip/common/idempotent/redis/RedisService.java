package com.eip.common.idempotent.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: RedisService
 * Function:
 * Date: 2021年12月07 15:56:47
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Component
public class RedisService {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate = redisTemplate;
    }

    public void set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }


    public void set(String key,String value,Long expireTime){
        redisTemplate.opsForValue().set(key,value,expireTime, TimeUnit.MILLISECONDS);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }


    public boolean exists(String key){
        return redisTemplate.hasKey(key);
    }


    public boolean remove(String key){
        return redisTemplate.delete(key);
    }

}
