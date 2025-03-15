package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.ProductDTO;
import com.funeral.entity.Category;
import com.funeral.entity.Product;
import com.funeral.mapper.ProductMapper;
import com.funeral.service.CategoryService;
import com.funeral.service.ProductService;
import com.funeral.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryService categoryService;

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
    public Page<Product> listProducts(Integer page, Integer size, Long categoryId) {
        Page<Product> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        
        if (categoryId != null) {
            queryWrapper.eq(Product::getCategoryId, categoryId);
        }
        
        queryWrapper.orderByDesc(Product::getCreatedTime);
        return productMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public List<Product> getRecommendedProducts(Integer type) {
        // 这里可以根据具体业务逻辑来实现推荐算法
        // 示例实现：获取置顶的商品作为推荐商品
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        List<Long> categoriesIds = categoryService.listCategoriesByType(type)
                .stream().map(Category::getId)
                .collect(Collectors.toList());
        queryWrapper
                // .eq(Product::getIsRecommended, true)
                .in(Product::getCategoryId, categoriesIds).orderByDesc(Product::getCreatedTime)
                   .last("LIMIT 10");
        return productMapper.selectList(queryWrapper);
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<ProductVO> getProductsByIds(List<Long> productIds) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Product::getId, productIds);
        List<Product> products = productMapper.selectList(queryWrapper);

        return products.stream().map(product -> {
            ProductVO vo = new ProductVO();
            BeanUtils.copyProperties(product, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}