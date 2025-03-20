package com.funeral.task;

import com.alibaba.fastjson.JSON;
import com.funeral.entity.CartItem;
import com.funeral.mapper.CartMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
public class CartSyncTask {
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Resource
    private CartMapper cartMapper;
    
    private static final String CART_KEY_PREFIX = "cart:";
    // 每7天执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    public void syncCartToDb() {
        Set<String> keys = stringRedisTemplate.keys(CART_KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }
        
        for (String key : keys) {
            String cartJson = stringRedisTemplate.opsForValue().get(key);
            if (cartJson != null) {
                Long userId = Long.valueOf(key.substring(CART_KEY_PREFIX.length()));
                List<CartItem> items = JSON.parseArray(cartJson, CartItem.class);
                
                // 同步到数据库
                cartMapper.deleteByUserId(userId);
                if (!items.isEmpty()) {
                    cartMapper.batchInsert(userId, items);
                }
            }
        }
    }
}
