package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.funeral.common.cache.ProductCache;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private ProductCache productCache;

    @Override
    @Transactional
    public void saveProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setIsEnabled(true); // 默认上架
        boolean success = productMapper.insert(product) > 0;
        
        if (success) {
            // 保存新商品到缓存
            productCache.saveProduct(product);
            log.info("商品保存成功并已添加到缓存，商品ID: {}", product.getId());
        }
    }

    @Transactional
    @Override
    public boolean saveProduct(Product product) {
        boolean success = save(product);
        if (success) {
            productCache.saveProduct(product);
        }
        return success;
    }

    @Transactional
    @Override
    public boolean updateProduct(Long id, ProductDTO productDTO) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        BeanUtils.copyProperties(productDTO, product);
        boolean success = productMapper.updateById(product) > 0;
        
        // 更新成功后，更新或清除缓存，确保数据一致性
        if (success) {
            // 更新缓存
            productCache.saveProduct(product);
            log.info("商品更新成功并已更新缓存，商品ID: {}", id);
        } else {
            // 更新失败，清除缓存，避免数据不一致
            productCache.removeProduct(id);
            log.warn("商品更新失败，已清除缓存，商品ID: {}", id);
        }
        return success;
    }

    @Transactional
    @Override
    public boolean updateStock(Long id, Integer delta) {
        // 直接从数据库获取最新商品数据
        Product product = productMapper.selectById(id);
        if (product == null || product.getStock() + delta < 0) {
            return false;
        }
        
        product.setStock(product.getStock() + delta);
        boolean success = productMapper.updateById(product) > 0;
        
        if (success) {
            // 更新库存缓存
            productCache.updateStock(id, product.getStock());
            // 同时更新商品完整信息缓存，确保数据一致性
            productCache.saveProduct(product);
            log.info("商品库存更新成功并已更新缓存，商品ID: {}, 库存变化: {}", id, delta);
        } else {
            // 更新失败，清除缓存避免数据不一致
            productCache.removeProduct(id);
            log.warn("商品库存更新失败，已清除缓存，商品ID: {}", id);
        }
        
        return success;
    }

    @Override
    public void deleteProduct(Long id) {
        boolean success = productMapper.deleteById(id) > 0;
        
        // 删除产品后，同时清除缓存
        productCache.removeProduct(id);
        log.info("商品删除操作完成，已清除缓存，商品ID: {}", id);
    }

    @Override
    public Product getProductById(Long id) {
        // 先尝试从缓存获取
        Optional<Product> optionalProduct = productCache.getProduct(id);
        
        // 缓存中存在则返回缓存数据
        if (optionalProduct.isPresent()) {
            log.debug("从缓存获取商品信息，商品ID: {}", id);
            return optionalProduct.get();
        }
        
        // 缓存未命中，从数据库查询最新数据
        log.debug("缓存未命中，从数据库获取商品信息，商品ID: {}", id);
        Product product = productMapper.selectById(id);
        
        // 将数据库查询结果更新到缓存
        if (product != null) {
            productCache.saveProduct(product);
            log.debug("已将商品信息更新到缓存，商品ID: {}", id);
        }
        
        return product;
    }

    @Override
    public Page<Product> listProducts(Integer page, Integer size, Long categoryId) {
        Page<Product> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        
        if (categoryId != null) {
            queryWrapper.eq(Product::getCategoryId, categoryId);
        }
        
        // 只查询上架的商品
        queryWrapper.eq(Product::getIsEnabled, true);
        
        // 按照排序字段和创建时间倒序排序
        queryWrapper.orderByDesc(Product::getCreatedTime);
        
        // 查询结果会自动设置total属性
        return productMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public Page<Product> listByCategory(Integer category, String subCategory, Integer page, Integer size) {
        Page<Product> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        
        // 查询指定分类下的商品
        List<Long> categoryIds = categoryService.listCategoriesByType(category)
                .stream().map(Category::getId)
                .collect(Collectors.toList());
                
        if (!categoryIds.isEmpty()) {
            queryWrapper.in(Product::getCategoryId, categoryIds);
        }
        
        // 只查询上架的商品
        queryWrapper.eq(Product::getIsEnabled, true);
        
        // 按照排序字段和创建时间倒序排序
        queryWrapper.orderByDesc( Product::getCreatedTime);
        
        // 查询结果会自动设置total属性
        return productMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public List<Product> getRecommendedProducts(Integer type) {
        // 这里可以根据具体业务逻辑来实现推荐算法
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        
        // 获取对应类型的分类ID列表
        List<Long> categoryIds = categoryService.listCategoriesByType(type)
                .stream().map(Category::getId)
                .collect(Collectors.toList());
                
        if (categoryIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        queryWrapper
                .eq(Product::getIsEnabled, true)  // 只查询上架商品
                .in(Product::getCategoryId, categoryIds)
                .orderByDesc(Product::getCreatedTime)
                .last("LIMIT 10");
                
        return productMapper.selectList(queryWrapper);
    }

    @Override
    public List<ProductVO> getProductsByIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Product> products = new ArrayList<>();
        List<Long> notCachedIds = new ArrayList<>();
        
        // 先尝试从缓存获取商品
        for (Long id : productIds) {
            Optional<Product> optionalProduct = productCache.getProduct(id);
            if (optionalProduct.isPresent()) {
                products.add(optionalProduct.get());
            } else {
                notCachedIds.add(id);
            }
        }
        
        // 对于缓存中不存在的商品，从数据库查询
        if (!notCachedIds.isEmpty()) {
            LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Product::getId, notCachedIds);
            List<Product> dbProducts = productMapper.selectList(queryWrapper);
            
            // 将查询到的商品保存到缓存并添加到结果列表
            for (Product product : dbProducts) {
                productCache.saveProduct(product);
                products.add(product);
            }
            
            log.debug("从数据库查询并缓存了{}个商品", dbProducts.size());
        }

        // 转换为VO对象
        return products.stream().map(product -> {
            ProductVO vo = new ProductVO();
            BeanUtils.copyProperties(product, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}