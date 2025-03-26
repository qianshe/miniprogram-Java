package com.funeral.service.impl;

import com.funeral.common.cache.CartCache;
import com.funeral.dto.CartDTO;
import com.funeral.entity.CartItem;
import com.funeral.entity.Product;
import com.funeral.service.CartService;
import com.funeral.service.ProductService;
import com.funeral.vo.CartItemVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartCache cartCache;

    @Resource
    private ProductService productService;

    @Override
    public void addToCart(Long userId, CartDTO cartDTO) {
        List<CartItem> items = cartCache.getCart(userId);
        if (items == null) {
            items = loadFromDb(userId);
        }

        Product product = productService.getProduct(cartDTO.getProductId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        CartItem item = new CartItem();
        item.setProductId(cartDTO.getProductId());
        item.setQuantity(cartDTO.getQuantity());
        item.setProductName(product.getName());
        item.setProductImage(product.getImageUrl());
        item.setPrice(product.getPrice());
        item.setTotalPrice(product.getPrice().multiply(new BigDecimal(cartDTO.getQuantity())));

        updateCart(items, item);

        cartCache.saveCart(userId, items);
    }

    @Override
    public List<CartItemVO> getUserCart(Long userId) {
        List<CartItem> items = cartCache.getCart(userId);
        if (items == null) {
            items = loadFromDb(userId);
            if (items != null) {
                cartCache.saveCart(userId, items);
            }
        }

        List<CartItemVO> cartItems = new ArrayList<>();
        for (CartItem item : items) {
            CartItemVO cartItemVO = new CartItemVO();
            cartItemVO.setProductId(item.getProductId());
            cartItemVO.setProductName(item.getProductName());
            cartItemVO.setProductImage(item.getProductImage());
            cartItemVO.setPrice(item.getPrice());
            cartItemVO.setQuantity(item.getQuantity());
            cartItemVO.setTotalPrice(item.getTotalPrice());
            cartItems.add(cartItemVO);
        }

        return cartItems;
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        List<CartItem> items = cartCache.getCart(userId);
        if (items == null) {
            items = loadFromDb(userId);
        }

        items.removeIf(item -> item.getProductId().equals(productId));

        cartCache.saveCart(userId, items);
    }

    @Override
    public void updateCartItemQuantity(Long userId, CartDTO cartDTO) {
        List<CartItem> items = cartCache.getCart(userId);
        if (items == null) {
            items = loadFromDb(userId);
        }

        for (CartItem item : items) {
            if (item.getProductId().equals(cartDTO.getProductId())) {
                if (cartDTO.getQuantity() <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(cartDTO.getQuantity());
                    item.setTotalPrice(item.getPrice().multiply(new BigDecimal(cartDTO.getQuantity())));
                }
                break;
            }
        }

        cartCache.saveCart(userId, items);
    }

    @Override
    public void clearCart(Long userId) {
        cartCache.removeCart(userId);
    }

    private List<CartItem> loadFromDb(Long userId) {
        // 从数据库加载购物车数据的逻辑
        return new ArrayList<>();
    }

    private void updateCart(List<CartItem> items, CartItem item) {
        boolean found = false;
        for (CartItem cartItem : items) {
            if (cartItem.getProductId().equals(item.getProductId())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(item);
        }
    }
}
