package com.eip.common.limit.config;

import com.eip.common.limit.aspect.RedisLimitAspect;
import com.eip.common.limit.generator.DefaultRedisKeyGenerator;
import com.eip.common.limit.generator.RedisKeyGenerator;
import com.eip.common.limit.handler.LimitHandler;
import com.eip.common.limit.handler.LocalLimitHandler;
import com.eip.common.limit.handler.RedisLimitHandler;
import com.eip.common.limit.type.LimitType;
import com.eip.common.limit.type.LimitTypeService;
import com.eip.common.limit.type.LocalLimitType;
import com.eip.common.limit.type.RedisLimitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

@ConditionalOnClass(RedisAutoConfiguration.class)
@EnableConfigurationProperties(EnableLiimitProperties.class)
@Import({RedisLimitAspect.class})
@ConditionalOnProperty(prefix = "pzy.redis.limit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisLimitAutoConfiguration {

    @Autowired
    private EnableLiimitProperties prop;

    @Bean
    @ConditionalOnMissingBean
    public RedisKeyGenerator redisKeyGenerator() {
        return new DefaultRedisKeyGenerator();
    }

    @Bean("redisLimitTemplate")
    @ConditionalOnProperty(prefix = "pzy.redis.limit", name = "type", havingValue = "redis")
    public RedisTemplate<String, Serializable> redisLimitTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> redisLimitTemplate = new RedisTemplate<>();
        redisLimitTemplate.setKeySerializer(new StringRedisSerializer());
        redisLimitTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisLimitTemplate.setConnectionFactory(redisConnectionFactory);
        return redisLimitTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public LimitHandler limitHandler(LimitTypeService limitTypeService){
        if(LimitType.REDIS.getName().equals(limitTypeService.getType())){
            return  new RedisLimitHandler();
        }else if(LimitType.LOCAL.getName().equals(limitTypeService.getType())){
            return  new LocalLimitHandler();
        }
        return  new LocalLimitHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public LimitTypeService limitTypeService(){
        String type = prop.getType();
        if(LimitType.REDIS.getName().equals(type)){
            return  new RedisLimitType(type);
        }else if(LimitType.LOCAL.getName().equals(type)){
            return  new LocalLimitType(type);
        }
        return  new LocalLimitType(type);
    }

}