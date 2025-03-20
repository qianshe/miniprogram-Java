package com.funeral.cache;

import com.funeral.entity.CartItem;
import java.util.List;

public interface CartCache {
    void saveCart(Long userId, List<CartItem> items);
    List<CartItem> getCart(Long userId);
    void removeCart(Long userId);

}
