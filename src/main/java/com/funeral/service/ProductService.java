package com.funeral.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.ProductDTO;
import com.funeral.entity.Product;

import java.util.List;

public interface ProductService {
    void saveProduct(ProductDTO productDTO);
    void updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    Product getProduct(Long id);
    Page<Product> listProducts(Integer page, Integer size, String category);
    
    /**
     * 获取推荐商品列表
     * @return 推荐商品列表
     */
    List<Product> getRecommendedProducts();
}