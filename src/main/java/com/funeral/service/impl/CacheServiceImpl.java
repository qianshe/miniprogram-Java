package com.funeral.service.impl;

import com.funeral.service.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
    
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
} 