package com.funeral.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.ProductDTO;
import com.funeral.entity.Product;
import com.funeral.vo.ProductVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {

    void saveProduct(ProductDTO productDTO);

    @Transactional
    boolean saveProduct(Product product);

    boolean updateProduct(Long id, ProductDTO productDTO);

    @Transactional
    boolean updateStock(Long id, Integer delta);

    void deleteProduct(Long id);
    Product getProductById(Long id);
    Page<Product> listProducts(Integer page, Integer size, Long categoryId);

    Page<Product> listByCategory(Integer category, String subCategory, Integer page, Integer size);

    /**
     * 获取推荐商品列表
     * @return 推荐商品列表
     */
    List<Product> getRecommendedProducts(Integer type);

    List<ProductVO> getProductsByIds(List<Long> productIds);
}