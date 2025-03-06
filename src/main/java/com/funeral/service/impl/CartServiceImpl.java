package com.funeral.service.impl;

import com.funeral.dto.CartDTO;
import com.funeral.entity.Product;
import com.funeral.service.CartService;
import com.funeral.service.ProductService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService {

    private static final String CART_PREFIX = "cart:";
    private static final long CART_EXPIRE_DAYS = 7;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ProductService productService;

    private String getCartKey(Long userId) {
        return CART_PREFIX + userId;
    }

    @Override
    public void addToCart(Long userId, CartDTO cartDTO) {
        String cartKey = getCartKey(userId);
        Product product = productService.getProduct(cartDTO.getProductId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String productId = String.valueOf(cartDTO.getProductId());
        Integer quantity = hashOps.get(cartKey, productId);
        
        if (quantity == null) {
            quantity = 0;
        }
        hashOps.put(cartKey, productId, quantity + cartDTO.getQuantity());
        redisTemplate.expire(cartKey, CART_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        String cartKey = getCartKey(userId);
        redisTemplate.opsForHash().delete(cartKey, String.valueOf(productId));
    }

    @Override
    public void updateCartItemQuantity(Long userId, CartDTO cartDTO) {
        String cartKey = getCartKey(userId);
        String productId = String.valueOf(cartDTO.getProductId());
        if (cartDTO.getQuantity() <= 0) {
            redisTemplate.opsForHash().delete(cartKey, productId);
        } else {
            redisTemplate.opsForHash().put(cartKey, productId, cartDTO.getQuantity());
        }
    }

    @Override
    public List<CartItemVO> getUserCart(Long userId) {
        String cartKey = getCartKey(userId);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cartKey);
        List<CartItemVO> cartItems = new ArrayList<>();
        
        entries.forEach((k, v) -> {
            Long productId = Long.valueOf(k.toString());
            Integer quantity = (Integer) v;
            Product product = productService.getProduct(productId);
            
            if (product != null) {
                CartItemVO cartItem = new CartItemVO();
                cartItem.setProductId(productId);
                cartItem.setProductName(product.getName());
                cartItem.setProductImage(product.getImageUrl());
                cartItem.setPrice(product.getPrice());
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
                cartItems.add(cartItem);
            }
        });
        
        return cartItems;
    }

    @Override
    public void clearCart(Long userId) {
        String cartKey = getCartKey(userId);
        redisTemplate.delete(cartKey);
    }
}
