package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.ProductDTO;
import com.funeral.entity.Product;
import com.funeral.mapper.ProductMapper;
import com.funeral.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Resource
    private ProductMapper productMapper;

    @Override
    public void saveProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setStatus(1); // 默认上架
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        BeanUtils.copyProperties(productDTO, product);
        productMapper.updateById(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public Product getProduct(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public Page<Product> listProducts(Integer page, Integer size, String category) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(category)) {
            wrapper.eq(Product::getCategoryId, category);
        }
        wrapper.eq(Product::getStatus, 1);
        wrapper.orderByDesc(Product::getCreatedTime);
        return productMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public List<Product> getRecommendedProducts() {
        // 这里可以根据具体业务逻辑来实现推荐算法
        // 示例实现：获取置顶的商品作为推荐商品
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                // .eq(Product::getIsRecommended, true)
                   .orderByDesc(Product::getCreatedTime)
                   .last("LIMIT 10");
        return productMapper.selectList(queryWrapper);
    }
}