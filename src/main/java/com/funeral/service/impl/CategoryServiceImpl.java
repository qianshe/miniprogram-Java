package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.funeral.dto.CategoryDTO;
import com.funeral.entity.Category;
import com.funeral.entity.ProductCategory;
import com.funeral.mapper.CategoryMapper;
import com.funeral.mapper.ProductCategoryMapper;
import com.funeral.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    @Resource
    private CategoryMapper categoryMapper;
    
    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public boolean saveCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        return categoryMapper.insert(category) > 0;
    }

    @Override
    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public Category getCategory(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<Category> listCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public List<Category> listCategoriesByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType, type);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public List<Category> listByType(Integer type) {
        log.debug("获取类型为 {} 的分类列表（旧版）", type);
        
        // 优先从新表查询
        List<ProductCategory> productCategories = productCategoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getType, type)
                        .eq(ProductCategory::getStatus, 1)
                        .orderByAsc(ProductCategory::getSort)
        );
        
        if (productCategories != null && !productCategories.isEmpty()) {
            log.debug("从新表查询到 {} 条记录", productCategories.size());
            // 转换为旧版Category实体
            return productCategories.stream().map(pc -> {
                Category category = new Category();
                category.setId(pc.getId());
                category.setName(pc.getName());
                category.setType(pc.getType());
                category.setSort(pc.getSort());
                return category;
            }).collect(Collectors.toList());
        }
        
        // 从旧表查询
        log.debug("从旧表查询分类列表");
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getType, type)
                        .orderByAsc(Category::getSort)
        );
    }
    
    @Override
    public List<ProductCategory> listProductCategories() {
        log.debug("获取所有商品分类列表（新版）");
        
        return productCategoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getStatus, 1)
                        .orderByAsc(ProductCategory::getSort)
        );
    }
    
    @Override
    public List<ProductCategory> listProductCategoriesByParentId(Long parentId) {
        log.debug("获取父分类ID为 {} 的子分类列表", parentId);
        
        return productCategoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getParentId, parentId)
                        .eq(ProductCategory::getStatus, 1)
                        .orderByAsc(ProductCategory::getSort)
        );
    }
    
    @Override
    public List<ProductCategory> getCategoryTree() {
        log.debug("获取分类树结构");
        
        // 获取所有分类
        List<ProductCategory> allCategories = productCategoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getStatus, 1)
                        .orderByAsc(ProductCategory::getSort)
        );
        
        // 构建父子关系
        Map<Long, List<ProductCategory>> parentMap = new HashMap<>();
        for (ProductCategory category : allCategories) {
            Long parentId = category.getParentId();
            if (!parentMap.containsKey(parentId)) {
                parentMap.put(parentId, new ArrayList<>());
            }
            parentMap.get(parentId).add(category);
        }
        
        // 获取顶级分类
        List<ProductCategory> rootCategories = parentMap.getOrDefault(0L, new ArrayList<>());
        
        // 递归构建树结构
        buildCategoryTree(rootCategories, parentMap);
        
        return rootCategories;
    }
    
    /**
     * 递归构建分类树
     */
    private void buildCategoryTree(List<ProductCategory> parentCategories, Map<Long, List<ProductCategory>> parentMap) {
        for (ProductCategory parent : parentCategories) {
            List<ProductCategory> children = parentMap.getOrDefault(parent.getId(), new ArrayList<>());
            if (!children.isEmpty()) {
                // 递归构建子分类的树结构
                buildCategoryTree(children, parentMap);
            }
        }
    }
    
    @Override
    public List<ProductCategory> listProductCategories(Integer type) {
        log.debug("获取类型为 {} 的分类列表（新版）", type);
        
        return productCategoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getType, type)
                        .eq(ProductCategory::getStatus, 1)
                        .orderByAsc(ProductCategory::getSort)
        );
    }
    
    @Override
    @Transactional
    public boolean saveProductCategory(ProductCategory productCategory) {
        log.debug("保存分类信息：{}", productCategory);
        // 保存到新表
        boolean success = productCategoryMapper.insert(productCategory) > 0;
        
        // 同步保存到旧表，保持兼容
        if (success) {
            Category category = new Category();
            category.setName(productCategory.getName());
            category.setType(productCategory.getType());
            category.setSort(productCategory.getSort());
            categoryMapper.insert(category);
            log.debug("同步保存到旧表完成");
        }
        
        return success;
    }
    
    @Override
    @Transactional
    public boolean updateProductCategory(ProductCategory productCategory) {
        log.debug("更新分类信息：{}", productCategory);
        // 更新新表
        boolean success = productCategoryMapper.updateById(productCategory) > 0;
        
        // 同步更新旧表，保持兼容
        if (success) {
            Category category = categoryMapper.selectById(productCategory.getId());
            if (category != null) {
                category.setName(productCategory.getName());
                category.setType(productCategory.getType());
                category.setSort(productCategory.getSort());
                categoryMapper.updateById(category);
                log.debug("同步更新旧表完成");
            }
        }
        
        return success;
    }
} 