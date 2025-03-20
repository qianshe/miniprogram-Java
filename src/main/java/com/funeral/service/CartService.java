package com.funeral.service;

import com.funeral.dto.CartDTO;
import com.funeral.vo.CartItemVO;

import java.util.List;

public interface CartService {
    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, CartDTO cartDTO);

    /**
     * 从购物车删除商品
     */
    void removeFromCart(Long userId, Long productId);

    /**
     * 更新购物车商品数量
     */
    void updateCartItemQuantity(Long userId, CartDTO cartDTO);

    /**
     * 获取用户购物车商品列表
     */
    List<CartItemVO> getUserCart(Long userId);

    /**
     * 清空用户购物车
     */
    void clearCart(Long userId);
}
