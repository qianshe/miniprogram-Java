package com.funeral.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.funeral.common.cache.CartCache;
import com.funeral.entity.CartItem;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MultiLevelCartCache implements CartCache {
    
    private final Cache<Long, List<CartItem>> localCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    private static final String CART_KEY_PREFIX = "cart:";
    
    @Override
    public void saveCart(Long userId, List<CartItem> items) {
        // 保存到本地缓存
        localCache.put(userId, items);
        // 保存到Redis
        stringRedisTemplate.opsForValue().set(
            CART_KEY_PREFIX + userId,
            JSON.toJSONString(items),
            1,
            TimeUnit.HOURS
        );
    }
    
    @Override
    public List<CartItem> getCart(Long userId) {
        // 先从本地缓存获取
        List<CartItem> items = localCache.getIfPresent(userId);
        if (items != null) {
            return items;
        }
        
        // 从Redis获取
        String cartJson = stringRedisTemplate.opsForValue().get(CART_KEY_PREFIX + userId);
        if (cartJson != null) {
            items = JSON.parseArray(cartJson, CartItem.class);
            // 放入本地缓存
            localCache.put(userId, items);
            return items;
        }
        
        return null;
    }
    
    @Override
    public void removeCart(Long userId) {
        localCache.invalidate(userId);
        stringRedisTemplate.delete(CART_KEY_PREFIX + userId);
    }
}
