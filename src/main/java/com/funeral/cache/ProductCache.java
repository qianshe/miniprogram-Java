package com.funeral.cache;

import com.funeral.entity.Product;
import java.util.Optional;

public interface ProductCache {
    void saveProduct(Product product);
    Optional<Product> getProduct(Long id);
    void removeProduct(Long id);
    void updateStock(Long id, Integer stock);
    Integer getStock(Long id);
}
