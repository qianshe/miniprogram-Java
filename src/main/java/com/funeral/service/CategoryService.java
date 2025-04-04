package com.funeral.service;

import com.funeral.dto.CategoryDTO;
import com.funeral.entity.Category;
import com.funeral.entity.ProductCategory;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    void saveCategory(CategoryDTO categoryDTO);
    void updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    Category getCategory(Long id);
    List<Category> listCategories();

    List<Category> listCategoriesByType(Integer type);

    /**
     * 根据类型获取分类列表
     * @param type 类型：0-白事，1-红事
     * @return 分类列表
     */
    List<Category> listByType(Integer type);

    /**
     * 获取新版商品分类列表
     * @param type 类型：0-白事，1-红事
     * @return 分类列表
     */
    List<ProductCategory> listProductCategories(Integer type);

    /**
     * 保存新版商品分类
     * @param productCategory 商品分类
     * @return 是否成功
     */
    boolean saveProductCategory(ProductCategory productCategory);

    /**
     * 更新新版商品分类
     * @param productCategory 商品分类
     * @return 是否成功
     */
    boolean updateProductCategory(ProductCategory productCategory);
}