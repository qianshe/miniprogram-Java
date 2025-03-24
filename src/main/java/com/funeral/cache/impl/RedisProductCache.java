package com.funeral.cache.impl;

import com.alibaba.fastjson.JSON;
import com.funeral.cache.ProductCache;
import com.funeral.entity.Product;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisProductCache implements ProductCache {
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    private static final String PRODUCT_KEY_PREFIX = "product:";
    private static final String STOCK_KEY_PREFIX = "product:stock:";
    private static final long CACHE_HOURS = 24;
    
    @Override
    public void saveProduct(Product product) {
        String key = PRODUCT_KEY_PREFIX + product.getId();
        String stockKey = STOCK_KEY_PREFIX + product.getId();
        
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(product), CACHE_HOURS, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(stockKey, product.getStock().toString(), CACHE_HOURS, TimeUnit.HOURS);
    }
    
    @Override
    public Optional<Product> getProduct(Long id) {
        String json = stringRedisTemplate.opsForValue().get(PRODUCT_KEY_PREFIX + id);
        return Optional.ofNullable(json).map(j -> JSON.parseObject(j, Product.class));
    }
    
    @Override
    public void removeProduct(Long id) {
        stringRedisTemplate.delete(PRODUCT_KEY_PREFIX + id);
        stringRedisTemplate.delete(STOCK_KEY_PREFIX + id);
    }
    
    @Override
    public void updateStock(Long id, Integer stock) {
        stringRedisTemplate.opsForValue().set(STOCK_KEY_PREFIX + id, stock.toString());
    }
    
    @Override
    public Integer getStock(Long id) {
        String stock = stringRedisTemplate.opsForValue().get(STOCK_KEY_PREFIX + id);
        return stock != null ? Integer.valueOf(stock) : null;
    }
}
